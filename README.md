MomRun 👩‍🍼💩

An endless runner Android game, built as Homework 1 for the UI Development course (10208) at Afeka.

A tired mom is carrying her crying baby down the hallway, and the floor is full of surprises. Dodge the poop, the dirty diapers and the toys for as long as you can!

Features
3-lane road: the mom can stand in the left, middle or right lane.
Left / right movement: two floating action buttons move the mom between lanes.
3 obstacle types: poop 💩, dirty diaper and toy 🧸, chosen at random.
Constant speed: obstacles move down one row on every game tick.
Crash notification: every hit shows a Toast message that matches the obstacle, and the phone vibrates.
3 lives: shown as pacifiers at the top of the screen.
Endless game: after losing all 3 lives, they refill and the game keeps going.
State saving: rotating the screen keeps the mom's lane, the lives and every obstacle in place.
Two languages: English and Hebrew (the app follows the device language).
How to Play
Obstacles fall from the top of the screen toward the mom.
Tap ⬅️ or ➡️ to switch lanes and avoid them.
Each hit costs one pacifier. When all three are gone, you get three new ones.
How It Works

The game does not use Canvas or x/y positioning. The screen is a fixed 6 × 3 grid of ImageViews built in XML, plus one row of 3 player images. Objects "move" by switching cells between VISIBLE and INVISIBLE.

On every tick of the game loop (Handler.postDelayed, every 600 ms), the GameManager:

Checks for a collision: is there an obstacle in the last row, in the player's lane?
Moves every row of obstacles one step down.
Spawns a new random obstacle in a random lane in the top row.

The game loop starts in onResume() and stops in onPause(), so the game pauses when the app goes to the background.

Project Structure
com.example.momrun
├── MainActivity          # Screen: finds views, handles clicks, game loop, refreshes the UI
├── logic/
│   └── GameManager       # Game rules: player lane, lives, obstacle grid, collisions
├── model/
│   └── ObstacleType      # Enum of obstacles, each with an image and a crash message
└── utilities/
└── Constants         # Game settings, timer values and Bundle keys
Demo

📹 A demo video is attached to the Moodle submission.

Built With
Kotlin
Android Studio
Material Components (FloatingActionButton)
Game images generated with Gemini, with backgrounds removed using remove.bg

Note: vibration cannot be felt on the Android Emulator. It works on a physical device.

Author

Tali Sustiel (TaliMichaeli)