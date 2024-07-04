let currentSlide = 0;
const slides = document.querySelectorAll('.slider img');
const totalSlides = slides.length;

function updateSlidePosition() {
    const slider = document.querySelector('.slider');
    slider.style.transform = `translateX(-${currentSlide * 100}%)`;
}

function prevSlide() {
    currentSlide = (currentSlide === 0) ? totalSlides - 1 : currentSlide - 1;
    updateSlidePosition();
}

function nextSlide() {
    currentSlide = (currentSlide === totalSlides - 1) ? 0 : currentSlide + 1;
    updateSlidePosition();
}