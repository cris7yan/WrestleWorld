document.addEventListener('DOMContentLoaded', () => {
    const applyButton = document.getElementById('apply-filters');
    const resetButton = document.getElementById('reset-filters');
    const endDateInput = document.getElementById('end-date');

    // Imposta la data odierna come valore predefinito per il campo end-date
    const today = new Date().toISOString().split('T')[0];
    endDateInput.setAttribute('max', today);
    endDateInput.value = today;

    if (applyButton) {
        applyButton.addEventListener('click', applyFilters);
    }

    if (resetButton) {
        resetButton.addEventListener('click', resetFilters);
    }
});

function applyFilters() {
    console.log('applyFilters function called'); // Debugging line

    const selectedFilters = getSelectedFilters();
    console.log('Selected Filters:', selectedFilters); // Debugging line

    if (!validatePrices(selectedFilters.minPrice, selectedFilters.maxPrice)) {
        resetFilters();
        return;
    }

    if (!validateDates(selectedFilters.startDate, selectedFilters.endDate)) {
        resetFilters();
        return;
    }

    const orders = document.querySelectorAll('.ordine');
    orders.forEach(order => {
        const orderData = getOrderData(order);
        console.log('Order Data:', orderData); // Debugging line

        const isVisible = isOrderVisible(orderData, selectedFilters);
        order.style.display = isVisible ? 'block' : 'none';
    });

    window.scrollTo(0, 0);
}

function getSelectedFilters() {
    return {
        minPrice: parseFloat(document.getElementById('min-price').value) || 0,
        maxPrice: parseFloat(document.getElementById('max-price').value) || Infinity,
        startDate: document.getElementById('start-date').value,
        endDate: document.getElementById('end-date').value
    };
}

function getOrderData(order) {
    return {
        price: parseFloat(order.getAttribute('data-prezzo')),
        date: new Date(order.getAttribute('data-data'))
    };
}

function isOrderVisible(orderData, filters) {
    return checkPrice(orderData.price, filters.minPrice, filters.maxPrice) &&
        checkDate(orderData.date, filters.startDate, filters.endDate);
}

function checkPrice(orderPrice, minPrice, maxPrice) {
    return orderPrice >= minPrice && orderPrice <= maxPrice;
}

function checkDate(orderDate, startDate, endDate) {
    if (!startDate && !endDate) return true;

    const start = new Date(startDate || '2024-01-01');
    const end = new Date(endDate || '9999-12-31');
    return orderDate >= start && orderDate <= end;
}

function validatePrices(minPrice, maxPrice) {
    if (minPrice < 0 || maxPrice < 0) {
        alert("Inserisci un valore valido, non può essere minore di 0");
        return false;
    }
    return true;
}

function validateDates(startDate, endDate) {
    const minDate = new Date('2024-01-01');
    const today = new Date();

    // Formatta la data odierna come dd-mm-aaaa
    const formattedToday = today.toLocaleDateString('it-IT', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    }).split('/').join('-');

    if (startDate && new Date(startDate) < minDate) {
        alert("Inserisci una data valida, non può essere una data precedente a 01-01-2024");
        return false;
    }

    if (endDate && new Date(endDate) > today) {
        alert("Inserisci una data valida, non può essere una data successiva a " + formattedToday);
        return false;
    }

    return true;
}

function resetFilters() {
    // Resetta i campi del prezzo
    document.getElementById('min-price').value = '';
    document.getElementById('max-price').value = '';

    // Resetta le date
    document.getElementById('start-date').value = '';
    document.getElementById('end-date').value = new Date().toISOString().split('T')[0];
}
