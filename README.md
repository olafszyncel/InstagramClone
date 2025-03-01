# 📌 SzyncPound

SzyncPound is a modern social media application that allows users to publish posts, comment, like content and follow other users. It is designed as a responsive web platform using Spring Boot.

---

## 📸 Screenshots

### 🔹 Home Page



### 🔹 Post View



### 🔹 Profile View



---

## 🚀 Features

- ✅ User registration and login (Spring Security, JWT)
- ✅ Posting with images
- ✅ Like and comment system
- ✅ User profile page
- ✅ Infinite scroll for posts
- ✅ User and post search
- ✅ Responsive design (CSS)

---

## 🛠️ Technologies

**Backend:**\
🔹 Java 17, Spring Boot 3\
🔹 Spring Security, JWT\
🔹 Hibernate, JPA, PostgreSQL\

**Frontend:**\
🔹 JavaScript\
🔹 CSS\

**Other:**\
🔹 Docker, Docker Compose\
🔹 CI/CD (GitHub Actions)

---

## ⚙️ Requirements

To run the application locally, you need:

- 📌 **Java 17**
- 📌 **Docker & Docker Compose**

---

## 🏗️ Installation & Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repository.git
   cd your-repository
   ```
2. Build the project:
   ```sh
   ./mvnw clean install
   ```
3. Run the application:
   ```sh
   ./mvnw spring-boot:run
   ```

## 🐳 Running Database in Docker Container

To start a PostgreSQL database in a Docker container, use the following command:

```sh
docker-compose up -d
```

---

## 📝 API Endpoints

📌 **Authentication**

- `POST /auth/register` – User registration
- `POST /auth/login` – Login and obtain JWT token

📌 **Posts**

- `GET /posts` – Fetch all posts (pagination)
- `POST /posts` – Create a new post
- `DELETE /posts/{id}` – Delete a post

📌 **Likes & Comments**

- `POST /posts/{id}/like` – Like a post
- `POST /posts/{id}/comment` – Add a comment

📌 **Users**

- `GET /users/{username}` – Fetch user profile
- `PUT /users/{id}` – Edit user profile

---

## 🔗 Contact

📩 Author: **Olaf Szyncel**\

