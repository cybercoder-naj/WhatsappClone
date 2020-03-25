
# WhatsappClone  
I am learning how to make a simple chat app using Firebase.  
   
## Checklist

 - [x] Setup
 - [x] Authentication with email and password
 - [x] User Information, Auto-Login and Logout
 - [x] TabLayout and ViewPager
 - [x] Display Users
 - [x] Setup Chat Activity
 - [x] Sending and displaying messages
 - [x] User profile, changing display photo
 - [x] User activity status
 - [x] Read receipts
 - [ ] Forget password
 - [ ] Sending Notifications

## Log  

### Day 1 (19/02/2020)

- **"Day 1 Commit"** and **"Day 1 Commit #2"**  
    
    I started this project yesterday. I have made the layout for the launcher screen, registration screen and login screen.  
    The code for creating a new user and signing in an existing user has also been added.

- **"Day 1 Commit #3"**
	
	Added the User information class - User.kt. It stores the Unique ID stored in Firebase along with the Username and image url. Added Auto login and log out.
	Added the Checklist heading in README.

### Day 2 (20/02/2020)
 
- **"Day 2 Commit #1"**
    
    I have added content to *activity_main.xml* - TabLayout, ViewPager with 2 Fragments, namely ChatsFragment and UsersFragment.
    I learnt new the new way of inflating the layout in Fragments. 
    Instead of:
	```kotlin
	class ChatsFragment : Fragment() {
		override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceBundle: Bundle?
		): View? =
			inflater.inflate(R.layout.fragment_chats, container, false)
	}
	```
    We use,
    ```kotlin
    class ChatsFragment : Fragment(R.layout.fragment_chats)
    ```
    This makes the code for fragment classes more readable as the constructor internally does the job of onCreateView
    
- **"Day 2 Commit #2"** and **Day 2 Commit #3**
    
    I have added the total number of users available on the firebase project server in the UsersFragment.
    
### Day 3 (22/02/2020)

- **"Day 3 Commit #1"**

    I have added the *MessageActivity.kt*. I learnt how to use scope functions efficiently.
    ```kotlin
    context.startActivity(
        MessageActivity.getIntent(context).apply {
            putExtra(context.resources.getStringExtra(R.string.intent_key_userid), user.id)
        }
    )
    ```
    
- **Day 3 Commit #2**

    Created items for texting and setup RecyclerView for displaying chats.

### Day 4 (23/02/2020)

- **"Day 4 Commit #1"**

	Not many changes today. I have added the code to ChatsFragment.kt so display the users you've had chats with.
	
### Day 5 (16/03/2020)

- **"Day 5 Commit #1"**

    I've returned back after a long time. I couldn't program because of my 11th standard annual examinations.
    Today I've added ViewBinding to all my Activities to ensure null and type safety. Although, I am not sure how
    I am gonna set my SupportActionBar with View Binding, so I'm still using Kotlin synthetics for that code only.
    I've added the profile tab in the app in which you can view your profile picture and username. You can now also
    change the profile picture from the image selected from your phone.
    
- **"Day 5 Commit #2"**

    Updated README.md
    
### Day 6 (18/03/2020)

- **"Day 6 Commit #1"**

    I have added a small amount of code that shows if the user is online or offline. A new field "status" is added to
    the realtime database which updates when the user becomes online or goes offline. An green color ImageView is 
    shown if *status == "online"* and hidden when *status == "offline"*.
    
- **"Day 6 Commit #2"**

    Added a search feature in *UsersFragment.kt*.
    
### Day 7 (25/03/2020)

- **"Day 7 Commit #1"**

    Now the user can see if the user has seen the message or not. A new field, *hasSeen* has been added to the realtime
    database which is read by the *MessageAdapter.kt*