// Carousels
const carousels = document.querySelectorAll('.carousel');
carousels.forEach((carousel, index) => {
    let currentSlide = 0;
    const slides = carousel.querySelectorAll('.slider img');
    const totalSlides = slides.length;

    function updateSlidePosition() {
        const slider = carousel.querySelector('.slider');
        slider.style.transform = `translateX(-${currentSlide * 100}%)`;
    }

    function prevSlide(carouselIndex) {
        currentSlide = (currentSlide === 0) ? totalSlides - 1 : currentSlide - 1;
        updateSlidePosition(carouselIndex);
    }

    function nextSlide(carouselIndex) {
        currentSlide = (currentSlide === totalSlides - 1) ? 0 : currentSlide + 1;
        updateSlidePosition(carouselIndex);
    }

    // Event listeners for navigation buttons
    const prevBtn = carousel.querySelector('.slider-nav-btn:nth-child(1)');
    const nextBtn = carousel.querySelector('.slider-nav-btn:nth-child(2)');
    prevBtn.addEventListener('click', () => prevSlide(index));
    nextBtn.addEventListener('click', () => nextSlide(index));
});
