# InventorySystem

# Overview
This project is an inventory management solution developed for Android mobile devices which stores product information, allowing users to view all products in inventory via a Recycler View Grid Layout, to add and completely remove products from the inventory system, to update product quantities, and to receive SMS and in-app notifications when any product runs out of stock.  This app was the term project for my Mobile Architecture and Development course at Southern New Hampshire University.  

![image](https://user-images.githubusercontent.com/88697660/208579369-71f53539-d62b-4b82-bc25-b89df22d4609.png)

# Requirements, Goals, and User Needs
The primary goal of this project was to provide a mobile app which would allow users within a company or organization to easily and efficiently manage products to be stocked in inventory.  The project requirements included Android platform designed specifically for mobile phones (no target API specified in requirements), a data model grounded in SQL, a login screen with username and password authentication, the ability to view inventory data through a grid layout, the ability to add new products to the inventory, the ability to completely delete products from the inventory, the ability to update in-stock quantities for each product in inventory, a way to adjust user preferences for receiving in-app and SMS messages, and out-of-stock notifications to be delivered to users based on their preferences.  The out-of-stock notifications are intended to help users know when to order additional product.  Since the app was going to be used on phones, the users needed the screens to be easy to read, and they needed the app itself to be intuitive and easy to use.  And for companies and organizations with large inventories, they needed to be able to load large numbers of products without bogging down the app.  

# User-Centered UI

The app includes a login screen, a new user account screen (triggered when the username is not found in the login table in the database), an inventory grid screen, a settings screen, and an update product quantity screen.  The inventory grid utilizes a Recycler View Grid Layout with CardViews for each product.  The deletion functionality was implemented using a contextual action bar on the inventory grid screen triggered by a long click.  Navigating between the screens is intuitive and driven by intents designed to establish and maintain a clean workflow.  The UI designs prioritized clarity and ease-of-use.

# Development Approach
I started development for this app with some rough sketches of the UI for different activities, then I started working on individual components following the MVC design pattern.  As I began coding each component, I performed some basic unit testing, followed by integration testing as soon as I had new components which could work together to conduct more complex functionality.  I tested frequently to make sure I stayed on the right track, and I wasn't afraid to step back and make some changes to components which I thought were completed but could benefit from some modifications.

# Testing Approach
I had hoped to implement JUNit tests during this project, but tight deadlines required me to make some difficult time-management decisions so I opted for testing larger components / component groups using the AVD emulator in Android Studio.  Frequest testing is the only way I could keep the project on track.  

# Meeting Development Challenges
I had to innovate frequently since this project led me into new territory almost every step of the way.  This was my first android app, so I spent dozens of hours pouring through documentation on the Android Developers site along with additional web research.  I took basic ideas and worked on grafting them into my project, finding many ways that didn't work, trying to understand why, then working on modifications which worked well.  Flexibility and dedication were my keywords for this project. 

# Project Highlights
I believe the database model and the controller for the login, new user, and inventory grid activities turned out rather well.  I endeavored to comment clearly and effectively and establish a solid foundation for future releases of the app as well as creating my own framework to modify and implement in other similar apps in the future.  And although the UI elements are all fairly basic, my primary takeaway was the necessity to make the elements of an app easy to read and interactions with the app easy to perform and intuitive.  I plan to use this starting point as a springboard for developing many more mobile apps in the future.  
