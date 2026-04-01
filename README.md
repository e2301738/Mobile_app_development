# Mobile_app_development

###Assignment I (UI Configuration)

Problems

Modify Example 2-1 so that during each stage of the main activity the elapsed time between subsequent lifesycle events is displayed on the log window.
Develop an Android application, which displays a form for collecting customer feedback. The feedback form should include appropriate labels and text fields for customer's first name, last name, phone number and comment as well as send and reset buttons. On the feedback form company's logo should also be displayed.
Update the application so that it can appears as a dialog.
Update the application so that the title can be hidden.

###Assignment II (Orientation Handling)

Problems

Create an Android application, which displays a registration form in both portrait and landscape mode. In portrait mode the registration form should consist of a logo and necessary labels and text fields for first name, last name, birthday and phone number. In landscape mode, the registration form should also display fields for entering home and  email addresses. Choose appropriate input types for the text fields. The application should also different logos on different orientations. In landscape mode the registration form should provide scrollbars, but in the portrait mode the whole form should be displayed in one single window. Make sure that if the screen orientation is changed while filling the registration form, already existing data in the text fields will no be lost. 
Create an Android application, which shows a random number each second on a label. The application should work so that each time the screen orientation changes, the current date and time will be saved to a variable and will be shown on a Toast later in the new orientation. The application should also show the latest random number before orientation change on a separate label. The application should still keep on displaying random numbers on the label reserved for showing random numbers.


###Assignment III (Event Listener)

Problems

Develop an application that displays a blog-style user interface with labeled input fields for user name and comment, along with a submit button. The application must support adding new blog entries and searching existing entries by text or date. Text-based searches must match both user names and comment content.

When submitting an entry, the application must validate the input fields and highlight any empty fields in red. Once the user interacts with a field, its background color should return to normal. If all fields are valid, the new entry is displayed together with previous entries in the same view.

Each entry must be shown in a clear format: entry number, submission date and time, user name, and comment. Entries must be ordered so that the most recent ones appear first. For searching, the application should provide appropriate input fields for text and date and display the matching blog entries accordingly.

###Assignment IV (Runtime UI)
 
Develop the following programs with Android:

An application in which you create the user interface during runtime. The application should consist of a label (TextView) to show text "Type your favourite number" and a text box (EditText) to which the user can enter his/her favorite number and then press Enter (Go) key on the device keyboard. After this, the application must generate a random number and compare it with the user's favorite number and display a Toast to inform whether the user's favorite number was the same as the generated random number.
An application in which you create the user interface during runtime. The application should provide a user interface for entering product information (id, name, unit price and total amount) and submitting them. After submitting product information, the application must write the data into a collection (like an ArrayList) and show a summary of available products and their total values (unit price multiplied by amount) on the same activity. Provide two buttons for the application; one for submitting data and the other one for clearing all text fields meant for entering product data. Provide scrollbars for both layouts to make sure that all application data can be viewed easily.


###Assignment V (AutoCompleteTextView)


An application which implements a phone catalog using AutoCompleteTextViews. The application should provide two separate areas for submitting and searching personal data. After submitting data, the application should make three different combinations of given personal data internally and  allow searching full personal data by typing first characters from the first name, the last name or  the phone number only. This means that there should be separate fields searching based on first name, last name and phone number and the application should display only search results, where either the first name or last name or phone number starts with the given characters. After clicking the item in the list, the application must separate and show data in a clean and clear way, like: First name=Xerx, Last name=Achaemenid, Phone=030486465. Use the search mechanism built in AutoCompleteTextView views. You should not write the code for searching data yourself! Hint: To make sure that the search functionality of AutoCompleteTextView works fine, do not add white spaces when combining different pieces of data together.

Improve your solution for previous problems so that the application allows collecting other useful data about people, like education level (as a combo box), hobbies (as a group of check boxes) and then save them all together as one string and allow searching full personal information by typing the first few characters from the first name, last name or educational level. Use AutoCompleteTextView views and do not implement search code yourself.

###Assignment VI (RecyclerView, Spinner, TimePicker, DatePicker)

An application which implements an even planner. The application should provide two separate areas for submitting and searching event data. To add events, the application should provide a user interface, which consists of a Spinner to select event type (like meeting, birthday, work or school), a DatePicker to select event date and a TimePicker to select the event time and a Submit button, which will submit the event data and then display them on a RecyclerView, like in the following: 
Meeting – 15 Mar – 09:00
Birthday Party – 18 Mar – 18:00
Lecture – 20 Mar – 10:00

###Assignment VII (Intent)

A Meeting Calendar application, which consists of several activities for entering, searching displaying and updating data. The application should provide necessary GUIs for entering the title of the meeting, the place, the list of participants and the date and time of the meeting. To keep meeting data, define Meeting class, which contains necessary attributes and methods. On the page for entering meeting data there should be a Summary, Search and Update buttons. Clicking Submit button should invoke another activity, where current and previous meeting data will be displayed.  Clicking Search button should invoke Search activity, which allows searching meeting data based on title, date, time and the name of the participants. Search result should include all the data related to the searched meeting. Clicking Update button should invoke the activity, which allows searching existing meeting data and changing any piece of it's data and saving it permanently. All activities (windows) should be linked to each other so that it is possible to navigate between them smoothly. Hint: define Meeting class as the data model and MeetingManager class, which includes the collection for keeping meeting data as well as all necessary methods for adding, searching, updating and deleting meeting data.








