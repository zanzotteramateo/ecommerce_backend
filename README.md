# 🛒 Ecommerce API Backend

Este repositorio contiene el backend para una plataforma de comercio electrónico de productos tecnológicos. Proporciona una API RESTful segura para gestionar productos, usuarios, carritos de compra y órdenes.

## 🚀 Tecnologías Utilizadas

* **Lenguaje:** Java
* **Framework:** Spring Boot
* **Base de Datos:** MySQL
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Gestión de dependencias:** Maven

## ✨ Características Principales

* Autenticación y autorización de usuarios (Registro y Login con JWT).
* Endpoints públicos para consultar el catálogo de productos tecnológicos.
* Endpoints privados (requieren token) para gestión del carrito de compras y checkout.
* Arquitectura en capas (Controladores, Servicios, Repositorios).

## 🔑 Credenciales de Prueba (Admin)

Para facilitar las pruebas de los endpoints protegidos, el sistema cuenta con un usuario administrador configurado por defecto desde el código. Puedes utilizar las siguientes credenciales para generar un token JWT con permisos de administrador:

* **Usuario / Email:** `admin@gmail`
* **Contraseña:** `admin123`

## 🛠️ Cómo ejecutar el proyecto localmente

Sigue estos pasos para levantar la API en tu entorno local:

1. **Clonar el repositorio:**
```bash
   git clone [https://github.com/TU_USUARIO/ecommerce_backend.git](https://github.com/TU_USUARIO/ecommerce_backend.git)
