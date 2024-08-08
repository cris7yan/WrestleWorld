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
        price: document.querySelector('input[name="price"]:checked')?.id || null,
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
    return checkPrice(orderData.price, filters.price) &&
        checkDate(orderData.date, filters.startDate, filters.endDate);
}

function checkPrice(orderPrice, filterPrice) {
    if (!filterPrice) return true;

    const [minPrice, maxPrice] = getPriceRange(filterPrice);
    return orderPrice >= minPrice && orderPrice <= maxPrice;
}

function getPriceRange(priceFilter) {
    switch (priceFilter) {
        case '0-50': return [0, 50];
        case '51-100': return [51, 100];
        case '101-500': return [101, 500];
        case '501-': return [501, Infinity];
        default: return [0, Infinity];
    }
}

function checkDate(orderDate, startDate, endDate) {
    if (!startDate && !endDate) return true;

    const start = new Date(startDate || '2024-01-01');
    const end = new Date(endDate || '9999-12-31');
    return orderDate >= start && orderDate <= end;
}

function validateDates(startDate, endDate) {
    const minDate = new Date('2024-01-01');
    const today = new Date();

    if (startDate && new Date(startDate) < minDate) {
        alert("Inserisci una data valida, non può essere una data precedente a 01-01-2024");
        return false;
    }

    if (endDate && new Date(endDate) > today) {
        alert("Inserisci una data valida, non può essere una data successiva a " + today);
        return false;
    }

    return true;
}

function resetFilters() {
    // Resetta i radio button
    document.querySelectorAll('input[type="radio"]').forEach(radio => {
        radio.checked = false;
    });

    // Resetta le date
    document.getElementById('start-date').value = '';
    document.getElementById('end-date').value = new Date().toISOString().split('T')[0];
}
