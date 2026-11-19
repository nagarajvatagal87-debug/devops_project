document.addEventListener('DOMContentLoaded', () => {
    // DOM elements
    const badgeStatus = document.getElementById('badge-status');
    const badgeEnv = document.getElementById('badge-env');
    const metricHealth = document.getElementById('metric-health');
    const metricUptime = document.getElementById('metric-uptime');
    const metricJava = document.getElementById('metric-java');
    const metricDocker = document.getElementById('metric-docker');

    const infoOs = document.getElementById('info-os');
    const infoCpus = document.getElementById('info-cpus');
    const infoMemory = document.getElementById('info-memory');

    const probeTarget = document.getElementById('probe-target');
    const probeTime = document.getElementById('probe-time');
    const probeOutput = document.getElementById('probe-output');

    const btnCheckHealth = document.getElementById('btn-check-health');
    const btnCheckPing = document.getElementById('btn-check-ping');
    const btnCheckActuator = document.getElementById('btn-check-actuator');

    const statTotal = document.getElementById('stat-total');
    const statCompleted = document.getElementById('stat-completed');
    const statInProgress = document.getElementById('stat-in-progress');
    const statPending = document.getElementById('stat-pending');
    const tasksTbody = document.getElementById('tasks-tbody');

    // Modal elements
    const taskModal = document.getElementById('task-modal');
    const btnOpenModal = document.getElementById('btn-open-modal');
    const btnCloseModal = document.getElementById('btn-close-modal');
    const btnCancelModal = document.getElementById('btn-cancel-modal');
    const taskForm = document.getElementById('task-form');
    const modalTitle = document.getElementById('modal-title');
    const taskIdInput = document.getElementById('task-id');
    const taskTitleInput = document.getElementById('task-title');
    const taskDescInput = document.getElementById('task-desc');
    const taskPriorityInput = document.getElementById('task-priority');
    const taskStatusInput = document.getElementById('task-status');

    // 1. Fetch System & Health Info
    async function loadHealthAndMetrics() {
        try {
            const res = await fetch('/api/health');
            if (res.ok) {
                const data = await res.json();
                metricHealth.textContent = data.status;
                badgeStatus.innerHTML = `<span class="dot"></span> System ${data.status}`;
                badgeEnv.textContent = data.environment;
                metricUptime.textContent = `${data.uptimeSeconds}s`;

                const isDocker = data.details?.dockerRunning;
                metricDocker.textContent = isDocker ? 'Running (Container)' : 'Host / Local';
                if (isDocker) {
                    metricDocker.classList.add('text-success');
                }
            }
        } catch (err) {
            console.warn('Health check failed:', err);
            metricHealth.textContent = 'DOWN';
            metricHealth.className = 'metric-val text-danger';
            badgeStatus.innerHTML = `<span class="dot"></span> Offline`;
            badgeStatus.className = 'badge badge-danger';
        }

        try {
            const res = await fetch('/api/system/info');
            if (res.ok) {
                const sys = await res.json();
                infoOs.textContent = `${sys.osName} (${sys.osArch})`;
                infoCpus.textContent = sys.availableProcessors;
                infoMemory.textContent = `${sys.freeMemoryMb} MB / ${sys.totalMemoryMb} MB`;
                metricJava.textContent = sys.javaVersion || '21';
            }
        } catch (err) {
            console.warn('System info failed:', err);
        }
    }

    // 2. Health Probe Trigger
    async function executeProbe(url, label) {
        probeTarget.textContent = `Endpoint: ${label}`;
        probeTime.textContent = 'Probing...';
        probeOutput.textContent = 'Sending request...';

        const startTime = performance.now();
        try {
            const res = await fetch(url);
            const duration = Math.round(performance.now() - startTime);
            probeTime.textContent = `${duration} ms`;

            const contentType = res.headers.get('content-type') || '';
            let bodyText;
            if (contentType.includes('application/json')) {
                const json = await res.json();
                bodyText = JSON.stringify(json, null, 2);
            } else {
                bodyText = await res.text();
            }

            probeOutput.textContent = `HTTP Status: ${res.status} ${res.statusText}\n\n${bodyText}`;
        } catch (err) {
            probeTime.textContent = 'Error';
            probeOutput.textContent = `Connection error: ${err.message}`;
        }
    }

    btnCheckHealth.addEventListener('click', () => executeProbe('/api/health', '/api/health'));
    btnCheckPing.addEventListener('click', () => executeProbe('/api/health/ping', '/api/health/ping'));
    btnCheckActuator.addEventListener('click', () => executeProbe('/actuator/health', '/actuator/health'));

    // 3. Load Tasks & Statistics
    async function loadTasks() {
        try {
            const [tasksRes, statsRes] = await Promise.all([
                fetch('/api/tasks'),
                fetch('/api/tasks/stats')
            ]);

            if (statsRes.ok) {
                const stats = await statsRes.json();
                statTotal.textContent = stats.total || 0;
                statCompleted.textContent = stats.completed || 0;
                statInProgress.textContent = stats.inProgress || 0;
                statPending.textContent = stats.pending || 0;
            }

            if (tasksRes.ok) {
                const tasks = await tasksRes.json();
                renderTasksTable(tasks);
            }
        } catch (err) {
            console.error('Error fetching tasks:', err);
            tasksTbody.innerHTML = `<tr><td colspan="6" class="text-center text-danger">Failed to load tasks: ${err.message}</td></tr>`;
        }
    }

    function renderTasksTable(tasks) {
        if (!tasks || tasks.length === 0) {
            tasksTbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted">No tasks defined yet. Click "+ Add New Task" to create one.</td></tr>`;
            return;
        }

        tasksTbody.innerHTML = tasks.map(task => {
            let statusBadgeClass = 'badge-secondary';
            if (task.status === 'COMPLETED') statusBadgeClass = 'badge-success';
            if (task.status === 'IN_PROGRESS') statusBadgeClass = 'badge-warning';
            if (task.status === 'PENDING') statusBadgeClass = 'badge-secondary';

            let priorityColor = 'text-muted';
            if (task.priority === 'HIGH') priorityColor = 'text-danger';
            if (task.priority === 'MEDIUM') priorityColor = 'text-warning';

            return `
                <tr>
                    <td style="font-family: var(--font-mono)">#${task.id}</td>
                    <td><strong>${escapeHtml(task.title)}</strong></td>
                    <td class="text-muted">${escapeHtml(task.description || '-')}</td>
                    <td><span class="${priorityColor}"><strong>${task.priority}</strong></span></td>
                    <td><span class="badge ${statusBadgeClass}">${task.status}</span></td>
                    <td>
                        <button class="btn btn-secondary btn-edit" data-id="${task.id}" style="padding: 4px 8px; margin-right: 4px;">Edit</button>
                        <button class="btn btn-danger btn-delete" data-id="${task.id}" style="padding: 4px 8px;">Delete</button>
                    </td>
                </tr>
            `;
        }).join('');

        // Attach action handlers
        document.querySelectorAll('.btn-edit').forEach(btn => {
            btn.addEventListener('click', () => openEditModal(btn.dataset.id));
        });

        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', () => deleteTask(btn.dataset.id));
        });
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>'"]/g, tag => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            "'": '&#39;',
            '"': '&quot;'
        }[tag] || tag));
    }

    // 4. Modal Handlers
    function openAddModal() {
        modalTitle.textContent = 'Add DevOps Milestone Task';
        taskIdInput.value = '';
        taskTitleInput.value = '';
        taskDescInput.value = '';
        taskPriorityInput.value = 'MEDIUM';
        taskStatusInput.value = 'PENDING';
        taskModal.classList.add('show');
    }

    async function openEditModal(id) {
        try {
            const res = await fetch(`/api/tasks/${id}`);
            if (res.ok) {
                const task = await res.json();
                modalTitle.textContent = `Edit Task #${task.id}`;
                taskIdInput.value = task.id;
                taskTitleInput.value = task.title;
                taskDescInput.value = task.description || '';
                taskPriorityInput.value = task.priority || 'MEDIUM';
                taskStatusInput.value = task.status || 'PENDING';
                taskModal.classList.add('show');
            }
        } catch (err) {
            alert('Could not fetch task: ' + err.message);
        }
    }

    function closeModal() {
        taskModal.classList.remove('show');
    }

    btnOpenModal.addEventListener('click', openAddModal);
    btnCloseModal.addEventListener('click', closeModal);
    btnCancelModal.addEventListener('click', closeModal);

    taskForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = taskIdInput.value;
        const payload = {
            title: taskTitleInput.value.trim(),
            description: taskDescInput.value.trim(),
            priority: taskPriorityInput.value,
            status: taskStatusInput.value
        };

        try {
            let res;
            if (id) {
                res = await fetch(`/api/tasks/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            } else {
                res = await fetch('/api/tasks', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            }

            if (res.ok) {
                closeModal();
                loadTasks();
            } else {
                alert('Failed to save task: ' + (await res.text()));
            }
        } catch (err) {
            alert('Error saving task: ' + err.message);
        }
    });

    async function deleteTask(id) {
        if (!confirm(`Are you sure you want to delete Task #${id}?`)) return;
        try {
            const res = await fetch(`/api/tasks/${id}`, { method: 'DELETE' });
            if (res.ok) {
                loadTasks();
            } else {
                alert('Failed to delete task.');
            }
        } catch (err) {
            alert('Error deleting task: ' + err.message);
        }
    }

    // Initial triggers & polling interval
    loadHealthAndMetrics();
    loadTasks();
    executeProbe('/api/health', '/api/health');

    // Poll health status every 15 seconds
    setInterval(loadHealthAndMetrics, 15000);
});
