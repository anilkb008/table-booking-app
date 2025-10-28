// Store bookings in localStorage
let bookings = JSON.parse(localStorage.getItem('bookings')) || [];

// Initialize the app
document.addEventListener('DOMContentLoaded', () => {
    displayBookings();
    setMinDate();
});

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