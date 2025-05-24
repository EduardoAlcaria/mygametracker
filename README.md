# 🎮 GamePlay Tracker (Beta)

A Java-based desktop application designed to **track your gameplay time automatically**.  
Currently in **beta**, it features a hybrid setup: a **CLI-driven workflow combined with System Tray interactions**, focused on background automation rather than visual interfaces.

---

## 🧠 Purpose

This app is being developed as a **personal project** to strengthen skills in:
- Java SE (Standard Edition)
- Process monitoring and OS integration
- MySQL with JDBC (DAO architecture)
- Multi-user session handling
- System Tray APIs
- Folder and process detection

---

## ⚙️ Features

✅ Login and user registration  
✅ Tracks when a specific game/process is running  
✅ Records and accumulates playtime in a MySQL database  
✅ Runs in the background with **System Tray** icon  
✅ Organized using DAO pattern and layered architecture

---

## 🚧 Missing / In Development

❌ Full GUI (JavaFX planned)  
❌ Game scraping from external APIs (e.g. Steam or HowLongToBeat)  
❌ Real-time statistics or reports  

---

## 💻 How to Run

1. Clone this repository  
2. Make sure you have:
   - JDK 17 or newer
   - MySQL running and credentials set up
3. Make sure to run the file as Adm, otherwise the program will not work as it should
4. Run `Main.java`
5. Use terminal + system tray to interact with the app

⚠️ Default database credentials can be edited in `ConnectionDAO.java`.

---

## 🗂️ Technologies Used

- Java 17+
- JavaFX (planned for future versions)
- MySQL
- JDBC
- Windows Task Manager commands
- System Tray APIs

---

## 🧩 Status

> 🚀 **Actively in development**  
> 📦 MVP version complete  
> 🎯 GUI + API integration coming soon

---

## 👤 Author

Eduardo – Software Engineering student @ PUCRS  
Project developed as personal practice & portfolio.

