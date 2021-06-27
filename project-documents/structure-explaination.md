# Project Structure

<p>The Project follows the patterned structure of Model, View, Controller.  Where the Model is the data pulled from the database into standard java object templates, the view encapsulates the various xml-based layouts that the controllers input the data into.</p>

<p>The structure is further mapped into sub patterned branches, such as the database interfaced with a repository, or an instanced database infrastructure layer.  This allows database access with CRUD (Create, Read, Updated, and Delete) methods at a controlled entry point, enables background and async tasks of the CRUD methods, and allows use of Android's LiveData format.</p>

> The LiveData framework assists in showing changes that are observed, or currently under the users view, preventing unnecessary cpu cycles for a phone like device.

<p>The UI, or user interface, structure of views and controllers involve fragement controllers (fragements utilize Android's fragemented, or interchangeable-content-view, layout views for interpolation of different UI elements) as the main view controllers that control user actions or clicks, Android's view-model framework for the DAOs, or data access objects, loading into the views elements to utilize LiveData, and adapters, or list-view-adapters, to control the lists of data present under a user's view into the contolling fragement controller.</p>

>To overview the UI structure, the focused views that are being called by the user, call their appointed fragments to setup actions and clicks; 
    ```there are three main trees of fragements: Assessment, Course, and Term, and each have edit and detail fragments, with various interchangable list fragements.```
The fragement's specific list-view-adapter will then call the appointed view-model to load the appropriate DAOs.  If any action the fragement controls requires an outside api or specific parameters, the utilities assist by forming appropriate interfaces decoupled from the main structure.
