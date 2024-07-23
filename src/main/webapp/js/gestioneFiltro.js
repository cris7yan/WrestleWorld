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

document.addEventListener('DOMContentLoaded', (event) => {
    const applyButton = document.getElementById('apply-filters');
    if (applyButton) {
        applyButton.addEventListener('click', applyFilters);
    }
});

function applyFilters() {
    console.log('applyFilters function called'); // Debugging line

    // Raccolta dei filtri selezionati
    const selectedFilters = {
        gender: document.querySelector('input[name="gender"]:checked')?.id || null,
        categories: [...document.querySelectorAll('.filters input[type="checkbox"]:checked')]
            .map(el => categoryMapping[el.id] || el.id)
            .filter(el => Object.values(categoryMapping).includes(el)),
        brands: [...document.querySelectorAll('.filters input[type="checkbox"]:checked')]
            .map(el => brandMapping[el.id] || el.id)
            .filter(el => Object.values(brandMapping).includes(el)),
        price: document.querySelector('input[name="price"]:checked')?.id || null,
        onSale: document.getElementById('on-sale').checked,
        signed: document.getElementById('Firmato').checked,
        available: document.getElementById('disponibile').checked
    };

    console.log('Selected Filters:', selectedFilters); // Debugging line

    // Selezione di tutti i prodotti
    const products = document.querySelectorAll('.product');

    // Applicazione dei filtri a ciascun prodotto
    products.forEach(product => {
        const gender = product.getAttribute('data-gender');
        const category = product.getAttribute('data-category');
        const brand = product.getAttribute('data-brand');
        const price = parseFloat(product.getAttribute('data-price'));
        const onSale = product.getAttribute('data-on-sale') === 'true';
        const signed = product.getAttribute('data-signed') === 'true';
        const available = product.getAttribute('data-availability') === 'true';

        console.log('Product Data:', {
            gender,
            category,
            brand,
            price,
            onSale,
            signed,
            available
        }); // Debugging line

        let isVisible = true;

        // Check gender
        if (selectedFilters.gender && selectedFilters.gender !== gender && gender !== "") {
            isVisible = false;
        }

        // Check categories
        if (selectedFilters.categories.length && !selectedFilters.categories.includes(category)) {
            console.log(`Category filter: ${selectedFilters.categories}, Product category: ${category}`); // Debugging line
            isVisible = false;
        }

        // Check brands
        if (selectedFilters.brands.length && !selectedFilters.brands.includes(brand)) {
            console.log(`Brand filter: ${selectedFilters.brands}, Product brand: ${brand}`); // Debugging line
            isVisible = false;
        }

        // Check price
        if (selectedFilters.price) {
            let minPrice = 0;
            let maxPrice = Infinity;

            switch (selectedFilters.price) {
                case '0-50':
                    minPrice = 0;
                    maxPrice = 50;
                    break;
                case '51-100':
                    minPrice = 51;
                    maxPrice = 100;
                    break;
                case '101-500':
                    minPrice = 101;
                    maxPrice = 500;
                    break;
                case '501-':
                    minPrice = 501;
                    maxPrice = Infinity;
                    break;
            }

            console.log(`Price filter: ${selectedFilters.price}, Product price: ${price}, Min price: ${minPrice}, Max price: ${maxPrice}`); // Debugging line

            if (price < minPrice || price > maxPrice) {
                isVisible = false;
            }
        }

        // Check on sale
        if (selectedFilters.onSale && !onSale) {
            isVisible = false;
        }

        // Check signed
        if (selectedFilters.signed && !signed) {
            isVisible = false;
        }

        // Check availability
        if (selectedFilters.available && !available) {
            isVisible = false;
        }

        // Set visibility
        product.style.display = isVisible ? 'block' : 'none';
    });

    // Scroll to the top of the page
    window.scrollTo(0, 0);
}
