document.addEventListener('DOMContentLoaded', () => {
    const applyButton = document.getElementById('apply-filters');
    const resetButton = document.getElementById('reset-filters');

    if (applyButton) {
        applyButton.addEventListener('click', applyFilters);
    }

    if (resetButton) {
        resetButton.addEventListener('click', resetFilters);
    }
});

function applyFilters() {
    const selectedFilters = getSelectedFilters();
    const users = document.querySelectorAll('.user-box');

    users.forEach(user => {
        const userData = getUserData(user);
        const isVisible = isUserVisible(userData, selectedFilters);

        user.style.visibility = isVisible ? 'visible' : 'hidden';
        user.style.position = isVisible ? 'relative' : 'absolute';
        user.style.opacity = isVisible ? '1' : '0';
        user.style.pointerEvents = isVisible ? 'auto' : 'none';
    });

    sortUsers(users, selectedFilters.orderBy);
}

function getSelectedFilters() {
    return {
        orderBy: document.getElementById('order-by').value,
        cognomeSearch: document.getElementById('cognome-search').value.toLowerCase(),
        startDate: document.getElementById('start-date').value,
        endDate: document.getElementById('end-date').value
    };
}

function getUserData(user) {
    return {
        nome: user.getAttribute('data-nome').toLowerCase(),
        cognome: user.getAttribute('data-cognome').toLowerCase(),
        dataNascita: new Date(user.getAttribute('data-datanascita'))
    };
}

function isUserVisible(userData, filters) {
    const matchesCognome = !filters.cognomeSearch || userData.cognome.includes(filters.cognomeSearch);
    const matchesDate = checkDate(userData.dataNascita, filters.startDate, filters.endDate);
    return matchesCognome && matchesDate;
}

function checkDate(userDate, startDate, endDate) {
    if (!startDate && !endDate) return true;

    const start = new Date(startDate || '1900-01-01');
    const end = new Date(endDate || '9999-12-31');
    return userDate >= start && userDate <= end;
}

function sortUsers(users, orderBy) {
    const container = document.querySelector('.container');
    const usersArray = Array.from(users);

    usersArray.sort((a, b) => {
        const aNome = a.getAttribute('data-nome').toLowerCase();
        const bNome = b.getAttribute('data-nome').toLowerCase();
        const aCognome = a.getAttribute('data-cognome').toLowerCase();
        const bCognome = b.getAttribute('data-cognome').toLowerCase();

        switch (orderBy) {
            case 'nome-asc':
                return aNome.localeCompare(bNome);
            case 'nome-desc':
                return bNome.localeCompare(aNome);
            case 'cognome-asc':
                return aCognome.localeCompare(bCognome);
            case 'cognome-desc':
                return bCognome.localeCompare(aCognome);
            default:
                return 0;
        }
    });

    // Rimuovi tutti gli utenti dal container e aggiungili nuovamente nell'ordine ordinato
    usersArray.forEach(user => container.appendChild(user));
}

function resetFilters() {
    document.getElementById('order-by').value = 'nome-asc';
    document.getElementById('cognome-search').value = '';
    document.getElementById('start-date').value = '';
    document.getElementById('end-date').value = '';

    applyFilters(); // Riapplica i filtri per ripristinare la vista originale
}
