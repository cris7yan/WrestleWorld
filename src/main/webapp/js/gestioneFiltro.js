function applyFilters() {
    // Recupera i filtri selezionati
    const selectedFilters = {
        gender: document.querySelector('input[name="gender"]:checked')?.id || null,
        categories: [...document.querySelectorAll('.filters input[type="checkbox"]:checked')].map(el => categoryMapping[el.id] || el.id),
        brands: [...document.querySelectorAll('.filters input[type="checkbox"]:checked')].map(el => el.id),
        price: document.querySelector('input[name="price"]:checked')?.id || null,
        onSale: document.getElementById('on-sale').checked
    };

    console.log('Selected Filters:', selectedFilters); // Debugging line

    const products = document.querySelectorAll('.product');

    products.forEach(product => {
        const gender = product.getAttribute('data-gender');
        const category = product.getAttribute('data-category');
        const brand = product.getAttribute('data-brand');
        const price = parseFloat(product.getAttribute('data-price'));
        const onSale = product.getAttribute('data-on-sale') === 'true';

        console.log('Product Data:', {
            gender,
            category,
            brand,
            price,
            onSale
        }); // Debugging line

        let isVisible = true;

        // Check gender
        if (selectedFilters.gender && selectedFilters.gender !== gender) {
            isVisible = false;
        }

        // Check categories
        if (selectedFilters.categories.length && !selectedFilters.categories.includes(category)) {
            console.log(`Category filter: ${selectedFilters.categories}, Product category: ${category}`); // Debugging line
            isVisible = false;
        }

        // Check brands
        if (selectedFilters.brands.length && !selectedFilters.brands.includes(brand)) {
            isVisible = false;
        }

        // Check price
        if (selectedFilters.price) {
            const priceRange = selectedFilters.price.split('-').map(Number);
            const minPrice = priceRange[0];
            const maxPrice = priceRange[1] || Infinity;

            if (price < minPrice || price > maxPrice) {
                isVisible = false;
            }
        }

        // Check on sale
        if (selectedFilters.onSale && !onSale) {
            isVisible = false;
        }

        // Set visibility
        product.style.display = isVisible ? 'block' : 'none';
    });
}
