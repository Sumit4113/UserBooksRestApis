# 📚 Online Bookstore

It's not completed yet , but i worked on that 
but i deployed this project like backend on Render and frontend on vercel 
here is the vercel live - https://onlinebookstorefrontend.vercel.app/

A full-featured online bookstore built using Spring Boot and Thymeleaf. Users can browse books by category, read books after login, and admins can manage book uploads. The platform supports secure login with JWT and OAuth2 authentication.

---

## 🚀 Features

### 👤 User Features
- Register and login (form-based + JWT + OAuth2 Google login)
- Browse books by genre (e.g., Fantasy, Philosophy, etc.)
- Read books online (PDF preview)
- Responsive homepage with category-wise book cards

### 🔐 Admin Features
- Admin dashboard with stats (total books, users)
- Upload books with:
  - Title
  - Author
  - Genre (category)
  - Book image
  - Book PDF
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`)

---

## 🧰 Tech Stack

| Layer          | Technology                             |
|----------------|----------------------------------------|
| Frontend       | Thymeleaf, HTML5, CSS3, JavaScript     |
| Backend        | Spring Boot, Spring Security, JWT, OAuth2 |
| Database       | MySQL                                  |
| ORM            | Hibernate, JPA                         |
| Deployment     | Docker, AWS EC2/S3 (Planned)           |
| Others         | Bootstrap, Font Awesome                |

---

## 🗂️ Project Structure

onlinebookstore/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com.example.onlinebookstore/
│ │ │ ├── controller/
│ │ │ ├── service/
│ │ │ ├── entity/
│ │ │ ├── repository/
│ │ │ ├── config/
│ │ │ └── OnlineBookstoreApplication.java
│ │ ├── resources/
│ │ ├── static/
│ │ ├── templates/
│ │ └── application.properties
├── Dockerfile (if using)
├── pom.xml
└── README.md


---

## 📸 Screenshots

> _Add screenshots or GIFs here showing homepage, login, admin upload panel, etc._

---

## 🔑 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/onlinebookstore.git
cd onlinebookstore
