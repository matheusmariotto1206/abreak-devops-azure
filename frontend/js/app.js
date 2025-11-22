// Estado da aplicação
const AppState = {
    currentUser: null,
    breaks: []
};

// ========== UTILITY ==========

function showLoading() {
    document.getElementById('loadingOverlay').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loadingOverlay').style.display = 'none';
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    setTimeout(() => toast.classList.remove('show'), 3000);
}

// ========== USERS ==========

async function loadUsers() {
    try {
        const response = await API.getAllUsers();
        const users = response.content || [];
        
        const select = document.getElementById('userSelect');
        select.innerHTML = '<option value="">Selecione um usuário...</option>';
        
        users.forEach(user => {
            const option = document.createElement('option');
            option.value = user.id;
            option.textContent = `${user.name} - ${user.email}`;
            select.appendChild(option);
        });
        
        document.getElementById('apiStatus').textContent = 'Conectado';
        document.getElementById('apiStatus').className = 'status-connected';
    } catch (error) {
        showToast('Erro ao carregar usuários', 'error');
        document.getElementById('apiStatus').textContent = 'Erro';
        document.getElementById('apiStatus').className = 'status-disconnected';
    }
}

async function selectUser(userId) {
    if (!userId) {
        document.getElementById('dashboard').style.display = 'none';
        document.getElementById('actionsSection').style.display = 'none';
        document.getElementById('breakListSection').style.display = 'none';
        return;
    }
    
    showLoading();
    try {
        // CONVERTER PARA NÚMERO!
        const user = await API.getUserById(parseInt(userId));
        AppState.currentUser = user;
        await loadUserData();
        
        document.getElementById('dashboard').style.display = 'block';
        document.getElementById('actionsSection').style.display = 'block';
        document.getElementById('breakListSection').style.display = 'block';
    } catch (error) {
        showToast('Erro ao carregar usuário', 'error');
        console.error(error);
    } finally {
        hideLoading();
    }
}

// ========== DASHBOARD ==========

async function loadUserData() {
    if (!AppState.currentUser || !AppState.currentUser.id) {
        console.error('Usuário não selecionado');
        return;
    }
    
    try {
        const userId = parseInt(AppState.currentUser.id);
        const breaksToday = await API.getBreaksToday(userId);
        const breaksHistory = await API.getUserBreaks(userId);
        
        AppState.breaks = breaksHistory.content || [];
        
        updateDashboard(breaksToday);
        renderBreakList();
    } catch (error) {
        console.error('Erro ao carregar dados:', error);
        showToast('Erro ao carregar dados', 'error');
    }
}

function updateDashboard(breaksToday) {
    const hidratacao = breaksToday.filter(b => b.breakType === 'HIDRATACAO').length;
    const alongamento = breaksToday.filter(b => b.breakType === 'ALONGAMENTO').length;
    const descanso = breaksToday.filter(b => b.breakType === 'DESCANSO_VISUAL').length;
    const total = breaksToday.length;
    const quota = AppState.currentUser.breaksQuota;
    
    document.getElementById('statHidratacao').textContent = hidratacao;
    document.getElementById('statAlongamento').textContent = alongamento;
    document.getElementById('statDescansoVisual').textContent = descanso;
    document.getElementById('statTotal').innerHTML = `${total} / <span id="statQuota">${quota}</span>`;
    
    const status = document.getElementById('statusQuota');
    if (total >= quota) {
        status.textContent = '✅ Meta alcançada!';
        status.className = 'status-ok';
    } else {
        status.textContent = '📊 Continue assim';
        status.className = 'status-ok';
    }
}

// ========== BREAKS ==========

function renderBreakList() {
    const container = document.getElementById('breakList');
    
    if (AppState.breaks.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📭</div>
                <p>Nenhuma pausa registrada ainda.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = '';
    AppState.breaks.forEach(breakItem => {
        const type = CONFIG.BREAK_TYPES[breakItem.breakType];
        const div = document.createElement('div');
        div.className = 'break-item fade-in';
        div.innerHTML = `
            <div class="break-info">
                <div>
                    <span class="break-type">${type.icon}</span>
                    <span class="break-title">${type.label}</span>
                </div>
                <div class="break-duration">Duração: ${breakItem.durationFormatted}</div>
            </div>
            <div class="break-actions">
                <button class="btn btn-edit" onclick="editBreak(${breakItem.id})">✏️ Editar</button>
                <button class="btn btn-danger" onclick="deleteBreak(${breakItem.id})">🗑️ Deletar</button>
            </div>
        `;
        container.appendChild(div);
    });
}

async function createBreak(formData) {
    showLoading();
    try {
        await API.createBreak({
            userId: AppState.currentUser.id,
            breakType: formData.breakType,
            durationSeconds: parseInt(formData.duration)
        });
        
        showToast('✅ Pausa registrada!', 'success');
        await loadUserData();
        document.getElementById('breakForm').reset();
    } catch (error) {
        showToast('❌ Erro ao registrar pausa', 'error');
    } finally {
        hideLoading();
    }
}

async function deleteBreak(breakId) {
    if (!confirm('Deletar esta pausa?')) return;
    
    showLoading();
    try {
        await API.deleteBreak(breakId);
        showToast('✅ Pausa deletada!', 'success');
        await loadUserData();
    } catch (error) {
        showToast('❌ Erro ao deletar', 'error');
    } finally {
        hideLoading();
    }
}

async function editBreak(breakId) {
    const breakItem = AppState.breaks.find(b => b.id === breakId);
    if (!breakItem) return;
    
    const duration = prompt('Nova duração (30-200 segundos):');
    if (!duration || duration < 30 || duration > 200) {
        showToast('⚠️ Duração inválida', 'warning');
        return;
    }
    
    showLoading();
    try {
        await API.updateBreak(breakId, {
            userId: AppState.currentUser.id,
            breakType: breakItem.breakType,
            durationSeconds: parseInt(duration)
        });
        
        showToast('✅ Pausa atualizada!', 'success');
        await loadUserData();
    } catch (error) {
        showToast('❌ Erro ao atualizar', 'error');
    } finally {
        hideLoading();
    }
}

// ========== USER FORM ==========

async function createUser(formData) {
    showLoading();
    try {
        await API.createUser({
            name: formData.name,
            email: formData.email,
            password: formData.password,
            position: formData.position || null,
            breaksQuota: parseInt(formData.breaksQuota) || 6
        });
        
        showToast('✅ Usuário criado!', 'success');
        await loadUsers();
        document.getElementById('userForm').style.display = 'none';
        document.getElementById('userForm').reset();
    } catch (error) {
        showToast('❌ Erro ao criar usuário', 'error');
    } finally {
        hideLoading();
    }
}

// ========== EVENT LISTENERS ==========

document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
    
    document.getElementById('userSelect').addEventListener('change', (e) => {
        selectUser(e.target.value);
    });
    
    document.getElementById('breakForm').addEventListener('submit', (e) => {
        e.preventDefault();
        createBreak({
            breakType: document.getElementById('breakType').value,
            duration: document.getElementById('duration').value
        });
    });
    
    document.getElementById('refreshBtn').addEventListener('click', () => {
        if (AppState.currentUser) loadUserData();
    });
    
    document.getElementById('toggleUserForm').addEventListener('click', () => {
        const form = document.getElementById('userForm');
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
    });
    
    document.getElementById('cancelUserForm').addEventListener('click', () => {
        document.getElementById('userForm').style.display = 'none';
    });
    
    document.getElementById('userForm').addEventListener('submit', (e) => {
        e.preventDefault();
        createUser({
            name: document.getElementById('userName').value,
            email: document.getElementById('userEmail').value,
            password: document.getElementById('userPassword').value,
            position: document.getElementById('userPosition').value,
            breaksQuota: document.getElementById('breaksQuota').value
        });
    });
});

window.deleteBreak = deleteBreak;
window.editBreak = editBreak;