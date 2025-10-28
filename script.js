// Store bookings in localStorage
let bookings = JSON.parse(localStorage.getItem('bookings')) || [];
let currentMenuItems = menuData;

// Initialize the app
document.addEventListener('DOMContentLoaded', () => {
    initializeTabs();
    displayBookings();
    displayMenu(currentMenuItems);
    setMinDate();
    setupMenuFilters();
});

// Tab Navigation
function initializeTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const tabName = btn.getAttribute('data-tab');
            
            // Remove active class from all tabs
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));
            
            // Add active class to clicked tab
            btn.classList.add('active');
            document.getElementById(tabName).classList.add('active');
        });
    });
}

// Set minimum date to today
function setMinDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date').setAttribute('min', today);
}

// Handle form submission
document.getElementById('bookingForm').addEventListener('submit', (e) => {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const booking = {
        id: Date.now(),
        name: formData.get('name'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        date: formData.get('date'),
        time: formData.get('time'),
        guests: formData.get('guests'),
        occasion: formData.get('occasion'),
        notes: formData.get('notes'),
        createdAt: new Date().toISOString()
    };
    
    bookings.push(booking);
    localStorage.setItem('bookings', JSON.stringify(bookings));
    
    // Reset form
    e.target.reset();
    
    // Display success message
    alert('Table booked successfully! We look forward to serving you.');
    
    // Switch to bookings tab
    document.querySelector('[data-tab="bookings"]').click();
    
    // Refresh bookings list
    displayBookings();
});

// Display all bookings
function displayBookings() {
    const container = document.getElementById('bookingsContainer');
    
    if (bookings.length === 0) {
        container.innerHTML = '<div class="no-bookings">No bookings yet. Make your first reservation!</div>';
        return;
    }
    
    // Sort bookings by date and time
    const sortedBookings = bookings.sort((a, b) => {
        const dateA = new Date(a.date + ' ' + a.time);
        const dateB = new Date(b.date + ' ' + b.time);
        return dateB - dateA;
    });
    
    container.innerHTML = sortedBookings.map(booking => `
        <div class="booking-card">
            <h3>${booking.name}</h3>
            <p><strong>Email:</strong> ${booking.email}</p>
            <p><strong>Phone:</strong> ${booking.phone}</p>
            <p class="booking-time"><strong>Date & Time:</strong> ${formatDate(booking.date)} at ${formatTime(booking.time)}</p>
            <p><strong>Guests:</strong> ${booking.guests}</p>
            ${booking.occasion ? `<p><strong>Occasion:</strong> ${booking.occasion}</p>` : ''}
            ${booking.notes ? `<p><strong>Notes:</strong> ${booking.notes}</p>` : ''}
            <button class="btn-cancel" onclick="cancelBooking(${booking.id})">Cancel Booking</button>
        </div>
    `).join('');
}

// Format date for display
function formatDate(dateString) {
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

// Format time for display
function formatTime(timeString) {
    const [hours, minutes] = timeString.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour % 12 || 12;
    return `${displayHour}:${minutes} ${ampm}`;
}

// Cancel a booking
function cancelBooking(id) {
    if (confirm('Are you sure you want to cancel this booking?')) {
        bookings = bookings.filter(booking => booking.id !== id);
        localStorage.setItem('bookings', JSON.stringify(bookings));
        displayBookings();
        alert('Booking cancelled successfully.');
    }
}

// Menu Functions
function displayMenu(items) {
    const container = document.getElementById('menuContainer');
    
    if (items.length === 0) {
        container.innerHTML = '<div class="no-results">No menu items found matching your criteria.</div>';
        return;
    }
    
    container.innerHTML = items.map(item => `
        <div class="menu-item" onclick="showMenuDetail(${item.id})">
            ${item.popular ? '<div class="popular-badge">Popular</div>' : ''}
            <div class="menu-item-image">${item.emoji}</div>
            <div class="menu-item-content">
                <span class="menu-item-category">${item.category}</span>
                <div class="menu-item-header">
                    <h3 class="menu-item-title">${item.name}</h3>
                    <span class="menu-item-price">$${item.price.toFixed(2)}</span>
                </div>
                <p class="menu-item-description">${item.description}</p>
                ${item.dietary.length > 0 ? `
                    <div class="menu-item-tags">
                        ${item.dietary.map(tag => `
                            <span class="dietary-tag ${tag}">${formatDietaryTag(tag)}</span>
                        `).join('')}
                    </div>
                ` : ''}
            </div>
        </div>
    `).join('');
}

function formatDietaryTag(tag) {
    return tag.split('-').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
}

function showMenuDetail(itemId) {
    const item = menuData.find(i => i.id === itemId);
    if (!item) return;
    
    const modal = document.getElementById('menuModal');
    const modalBody = document.getElementById('modalBody');
    
    modalBody.innerHTML = `
        <div class="modal-item-image">${item.emoji}</div>
        <span class="menu-item-category">${item.category}</span>
        <div class="menu-item-header" style="margin-top: 15px;">
            <h2 class="menu-item-title">${item.name}</h2>
            <span class="menu-item-price">$${item.price.toFixed(2)}</span>
        </div>
        <p class="menu-item-description" style="margin: 20px 0; font-size: 1.1rem;">${item.description}</p>
        ${item.dietary.length > 0 ? `
            <div class="menu-item-tags">
                ${item.dietary.map(tag => `
                    <span class="dietary-tag ${tag}">${formatDietaryTag(tag)}</span>
                `).join('')}
            </div>
        ` : ''}
        ${item.popular ? '<p style="margin-top: 20px; color: #ff9800; font-weight: 600;">⭐ Popular Choice!</p>' : ''}
    `;
    
    modal.classList.add('active');
}

// Close modal
document.querySelector('.close-modal').addEventListener('click', () => {
    document.getElementById('menuModal').classList.remove('active');
});

// Close modal when clicking outside
document.getElementById('menuModal').addEventListener('click', (e) => {
    if (e.target.id === 'menuModal') {
        document.getElementById('menuModal').classList.remove('active');
    }
});

// Setup menu filters
function setupMenuFilters() {
    const searchInput = document.getElementById('menuSearch');
    const categoryFilter = document.getElementById('categoryFilter');
    const dietaryFilter = document.getElementById('dietaryFilter');
    
    searchInput.addEventListener('input', filterMenu);
    categoryFilter.addEventListener('change', filterMenu);
    dietaryFilter.addEventListener('change', filterMenu);
}

function filterMenu() {
    const searchTerm = document.getElementById('menuSearch').value.toLowerCase();
    const category = document.getElementById('categoryFilter').value;
    const dietary = document.getElementById('dietaryFilter').value;
    
    let filtered = menuData;
    
    // Filter by search term
    if (searchTerm) {
        filtered = filtered.filter(item => 
            item.name.toLowerCase().includes(searchTerm) ||
            item.description.toLowerCase().includes(searchTerm)
        );
    }
    
    // Filter by category
    if (category !== 'all') {
        filtered = filtered.filter(item => item.category === category);
    }
    
    // Filter by dietary
    if (dietary !== 'all') {
        filtered = filtered.filter(item => item.dietary.includes(dietary));
    }
    
    displayMenu(filtered);
}