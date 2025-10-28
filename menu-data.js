// Restaurant Menu Data
const menuData = [
    // Appetizers
    {
        id: 1,
        name: "Bruschetta al Pomodoro",
        description: "Toasted bread topped with fresh tomatoes, garlic, basil, and extra virgin olive oil",
        price: 12.99,
        category: "appetizers",
        dietary: ["vegetarian", "vegan"],
        popular: true,
        emoji: "🍅"
    },
    {
        id: 2,
        name: "Crispy Calamari",
        description: "Lightly breaded and fried calamari served with marinara sauce and lemon",
        price: 14.99,
        category: "appetizers",
        dietary: [],
        popular: true,
        emoji: "🦑"
    },
    {
        id: 3,
        name: "Caesar Salad",
        description: "Crisp romaine lettuce, parmesan cheese, croutons, and classic Caesar dressing",
        price: 10.99,
        category: "appetizers",
        dietary: ["vegetarian"],
        popular: false,
        emoji: "🥗"
    },
    {
        id: 4,
        name: "Buffalo Wings",
        description: "Spicy chicken wings served with blue cheese dressing and celery sticks",
        price: 13.99,
        category: "appetizers",
        dietary: ["gluten-free"],
        popular: false,
        emoji: "🍗"
    },

    // Main Course
    {
        id: 5,
        name: "Grilled Salmon",
        description: "Fresh Atlantic salmon grilled to perfection, served with roasted vegetables and lemon butter sauce",
        price: 28.99,
        category: "mains",
        dietary: ["gluten-free"],
        popular: true,
        emoji: "🐟"
    },
    {
        id: 6,
        name: "Ribeye Steak",
        description: "12oz premium ribeye steak, grilled to your preference with garlic mashed potatoes and seasonal vegetables",
        price: 36.99,
        category: "mains",
        dietary: ["gluten-free"],
        popular: true,
        emoji: "🥩"
    },
    {
        id: 7,
        name: "Mushroom Risotto",
        description: "Creamy Arborio rice with mixed mushrooms, parmesan, and truffle oil",
        price: 22.99,
        category: "mains",
        dietary: ["vegetarian", "gluten-free"],
        popular: false,
        emoji: "🍄"
    },
    {
        id: 8,
        name: "Chicken Parmesan",
        description: "Breaded chicken breast topped with marinara sauce and melted mozzarella, served with pasta",
        price: 24.99,
        category: "mains",
        dietary: [],
        popular: true,
        emoji: "🍗"
    },
    {
        id: 9,
        name: "Spaghetti Carbonara",
        description: "Classic Roman pasta with eggs, pecorino cheese, guanciale, and black pepper",
        price: 19.99,
        category: "mains",
        dietary: [],
        popular: false,
        emoji: "🍝"
    },
    {
        id: 10,
        name: "Vegetable Buddha Bowl",
        description: "Quinoa bowl with roasted vegetables, avocado, chickpeas, and tahini dressing",
        price: 18.99,
        category: "mains",
        dietary: ["vegetarian", "vegan", "gluten-free"],
        popular: false,
        emoji: "🥙"
    },

    // Desserts
    {
        id: 11,
        name: "Tiramisu",
        description: "Classic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cream",
        price: 9.99,
        category: "desserts",
        dietary: ["vegetarian"],
        popular: true,
        emoji: "🍰"
    },
    {
        id: 12,
        name: "Chocolate Lava Cake",
        description: "Warm chocolate cake with a molten center, served with vanilla ice cream",
        price: 10.99,
        category: "desserts",
        dietary: ["vegetarian"],
        popular: true,
        emoji: "🍫"
    },
    {
        id: 13,
        name: "Crème Brûlée",
        description: "Classic French custard with a caramelized sugar topping",
        price: 8.99,
        category: "desserts",
        dietary: ["vegetarian", "gluten-free"],
        popular: false,
        emoji: "🍮"
    },
    {
        id: 14,
        name: "New York Cheesecake",
        description: "Rich and creamy cheesecake with graham cracker crust and berry compote",
        price: 9.99,
        category: "desserts",
        dietary: ["vegetarian"],
        popular: false,
        emoji: "🍰"
    },

    // Beverages
    {
        id: 15,
        name: "Fresh Squeezed Orange Juice",
        description: "Freshly squeezed orange juice",
        price: 5.99,
        category: "beverages",
        dietary: ["vegetarian", "vegan", "gluten-free"],
        popular: false,
        emoji: "🍊"
    },
    {
        id: 16,
        name: "Espresso",
        description: "Rich Italian espresso",
        price: 3.99,
        category: "beverages",
        dietary: ["vegetarian", "vegan", "gluten-free"],
        popular: true,
        emoji: "☕"
    },
    {
        id: 17,
        name: "Craft Beer Selection",
        description: "Ask your server for today's selection of local craft beers",
        price: 7.99,
        category: "beverages",
        dietary: ["vegetarian", "vegan"],
        popular: false,
        emoji: "🍺"
    },
    {
        id: 18,
        name: "House Red Wine",
        description: "Premium selection of red wine",
        price: 9.99,
        category: "beverages",
        dietary: ["vegetarian", "vegan", "gluten-free"],
        popular: true,
        emoji: "🍷"
    }
];