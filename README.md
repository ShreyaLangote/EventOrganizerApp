The Event Organizer app is a comprehensive Android application designed to simplify event planning and management. It includes tools for creating and managing events, sending invitations, tracking RSVPs, and managing guests. With its intuitive design, it caters to both casual users and professionals.

Features

Core Features:
- User Authentication:
  - Secure account creation and login.
  - Password encryption for protecting user credentials.
- Event Creation:
  - Add event details like title, description, date, time, location, and category.
  - Upload event cover photos or images.
  - Map integration for precise event locations.
- Guest Management:
  - Add guests by name or email.
  - Import contacts from the user’s device.
- Invitation Management:
  - Customize invitation messages.
  - Track RSVP statuses for each guest.
  - Set reminders for pending responses.
- Calendar and List Views:
  - Display upcoming and past events.
  - Filter events by date, category, or RSVP status.
- Notifications:
  - Get alerts for upcoming events.
  - Updates for RSVP responses or changes in event details.

Advanced Features:
- In-App Chat:
  - Direct communication with guests.
  - Group discussions for event planning.
- Guest List Management:
  - Assign seating arrangements.
  - Create and manage guest lists for multiple events.

Near Future Scope:
- Payment Integration:
  - Facilitate payments for ticketed events.
  - Secure transaction processing.
- Social Media Sharing:
  - Share event details on social media platforms.
- Location-Based Services:
  - Provide event location directions.
  - Suggest nearby venues for last-minute changes.

Project Structure

Activities:
- AddGuestDialog
- ChatActivity
- DashboardActivity
- Event
- EventAdapter
- EventCreationActivity
- ForgotPasswordActivity
- Guest
- GuestAdapter
- GuestManagementActivity
- Invitation
- InvitationActivity
- InvitationAdapter
- InvitationsFragment
- LoginActivity
- MainActivity
- Message
- MessageAdapter
- RSVPTrackingActivity
- SettingsActivity
- SignUpActivity
- SplashActivity
- User

Adapters:
- EventAdapter: For displaying events in a list or grid.
- GuestAdapter: For managing guest lists.

Utilities:
- EmailUtils: Handles sending email invitations.
- NotificationUtils: Manages push notifications.
- DateUtils: Provides date and time utilities.

Development Steps
1. Authentication:
   - Set up Firebase Authentication.
   - Create LoginActivity and SignUpActivity.
2. Event Management:
   - Design EventCreationActivity with input fields for event details.
   - Integrate Google Maps API for location selection.
3. Guest Management:
   - Create GuestManagementActivity for managing guests.
   - Implement contact import functionality using the Android contacts API.
4. Invitation Handling:
   - Use EmailUtils for sending email invitations.
   - Set up Firebase Cloud Messaging for notifications.
5. Event List:
   - Use RecyclerView with EventAdapter for event display.
   - Add filtering options.
6. Advanced Features:
   - Implement ChatActivity for messaging.
   - Integrate Stripe API for payment functionality.

How to Run the Project
1. Clone the repository: git clone ( https://github.com/ShreyaLangote/EventOrganizerApp.git )
2. Open the project in Android Studio.
3. Configure Firebase:
   - Add the google-services.json file to the app directory.
   - Enable Firebase services (Authentication, Firestore, Messaging).
4. Build and Run:
   - Connect an Android device or emulator.
   - Click the "Run" button in Android Studio.

Future Scope
- AI Integration:
  - Smart suggestions for event planning based on previous events.
- Cross-Platform Development:
  - Create an iOS version using Flutter or React Native.
- Analytics:
  - Provide insights like guest turnout and RSVP trends.
- Offline Mode:
  - Allow users to manage events without an internet connection.

Contributors
- Shreya Langote - Developer
- Contributions are welcome. Fork the repository and submit a pull request.

License
This project is licensed under the MIT License.
