// Configuração da API
const CONFIG = {
    // URL da API - localhost para teste local, depois muda pro IP da VM
    API_BASE_URL: 'http://localhost:8080/api',
    
    // Endpoints
    ENDPOINTS: {
        USERS: '/users',
        BREAKS: '/breaks',
        BREAKS_TODAY: '/breaks/today',
        BREAKS_USER: '/breaks/user'
    },
    
    // Tipos de pausa
    BREAK_TYPES: {
        HIDRATACAO: {
            label: 'Hidratação',
            icon: '💧',
            color: '#3b82f6'
        },
        ALONGAMENTO: {
            label: 'Alongamento',
            icon: '🤸',
            color: '#10b981'
        },
        DESCANSO_VISUAL: {
            label: 'Descanso Visual',
            icon: '👁️',
            color: '#f59e0b'
        }
    }
};

// Helper para construir URLs
function buildUrl(endpoint, params = {}) {
    let url = CONFIG.API_BASE_URL + endpoint;
    const queryParams = new URLSearchParams(params);
    if (queryParams.toString()) {
        url += '?' + queryParams.toString();
    }
    return url;
}

window.CONFIG = CONFIG;
window.buildUrl = buildUrl;