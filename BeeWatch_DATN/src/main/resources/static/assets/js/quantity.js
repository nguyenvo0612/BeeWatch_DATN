var quantityElement = document.getElementById('quantity');
var quantity = 0;

function updateQuantity() {
    quantityElement.innerText = quantity.toString();
}

function increaseQuantity() {
    quantity++;
    updateQuantity();
}

function decreaseQuantity() {
    if (quantity > 0) {
        quantity--;
        updateQuantity();
    }
}