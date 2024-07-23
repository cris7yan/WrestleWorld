const categoryMapping = {
    'Abbigliamento': 'Abbigliamento',
    'Accessori': 'Accessori',
    'Oggetti-da-collezione': 'Oggetti da collezione',
    'Title-Belts': 'Title Belts'
};

const brandMapping = {
    'brand1': 'Antigua',
    'brand2': 'Chalk Line',
    'brand3': 'Fanatics Authentic',
    'brand4': 'Fanatics Branded',
    'brand5': 'Funko Pop',
    'brand6': 'Keyscaper',
    'brand7': 'WWE Authentic'
};

document.addEventListener('DOMContentLoaded', () => {
    const applyButton = document.getElementById('apply-filters');
    if (applyButton) {
        applyButton.addEventListener('click', applyFilters);
    }
});

function applyFilters() {
    console.log('applyFilters function called'); // Debugging line

    const selectedFilters = getSelectedFilters();
    console.log('Selected Filters:', selectedFilters); // Debugging line

    const products = document.querySelectorAll('.product');
    products.forEach(product => {
        const productData = getProductData(product);
        console.log('Product Data:', productData); // Debugging line

        const isVisible = isProductVisible(productData, selectedFilters);
        product.style.display = isVisible ? 'block' : 'none';
    });

    window.scrollTo(0, 0);
}

function getSelectedFilters() {
    return {
        gender: document.querySelector('input[name="gender"]:checked')?.id || null,
        categories: getSelectedCheckboxes('.filters input[type="checkbox"]', categoryMapping),
        brands: getSelectedCheckboxes('.filters input[type="checkbox"]', brandMapping),
        price: document.querySelector('input[name="price"]:checked')?.id || null,
        onSale: document.getElementById('on-sale').checked,
        signed: document.getElementById('Firmato').checked,
        available: document.getElementById('disponibile').checked
    };
}

function getSelectedCheckboxes(selector, mapping) {
    return [...document.querySelectorAll(selector)]
        .filter(el => el.checked)
        .map(el => mapping[el.id] || el.id)
        .filter(el => Object.values(mapping).includes(el));
}

function getProductData(product) {
    return {
        gender: product.getAttribute('data-gender'),
        category: product.getAttribute('data-category'),
        brand: product.getAttribute('data-brand'),
        price: parseFloat(product.getAttribute('data-price')),
        onSale: product.getAttribute('data-on-sale') === 'true',
        signed: product.getAttribute('data-signed') === 'true',
        available: product.getAttribute('data-availability') === 'true'
    };
}

function isProductVisible(productData, filters) {
    return checkGender(productData.gender, filters.gender) &&
        checkCategories(productData.category, filters.categories) &&
        checkBrands(productData.brand, filters.brands) &&
        checkPrice(productData.price, filters.price) &&
        checkOnSale(productData.onSale, filters.onSale) &&
        checkSigned(productData.signed, filters.signed) &&
        checkAvailability(productData.available, filters.available);
}

function checkGender(productGender, filterGender) {
    return !filterGender || filterGender === productGender || productGender === "";
}

function checkCategories(productCategory, filterCategories) {
    return !filterCategories.length || filterCategories.includes(productCategory);
}

function checkBrands(productBrand, filterBrands) {
    return !filterBrands.length || filterBrands.includes(productBrand);
}

function checkPrice(productPrice, filterPrice) {
    if (!filterPrice) return true;

    const [minPrice, maxPrice] = getPriceRange(filterPrice);
    return productPrice >= minPrice && productPrice <= maxPrice;
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

function checkOnSale(productOnSale, filterOnSale) {
    return !filterOnSale || productOnSale;
}

function checkSigned(productSigned, filterSigned) {
    return !filterSigned || productSigned;
}

function checkAvailability(productAvailable, filterAvailable) {
    return !filterAvailable || productAvailable;
}
