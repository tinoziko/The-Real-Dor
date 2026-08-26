<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](#)
[![ONNX](https://img.shields.io/badge/ONNX-005CED?style=for-the-badge&logo=onnx&logoColor=white)](#)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](#)

</div>

# The Real d'Or: AI-Powered Football Analytics Dashboard

This repository contains the source code for a thesis project developed at Vistula University, designed to objectively evaluate and rank elite football players using advanced statistical metrics and machine learning.

## System Architecture
The application acts as a robust data pipeline bridging live sports statistics and predictive AI modeling. It aggregates raw match data across all domestic and continental competitions to evaluate players on their actual on-pitch influence, moving beyond basic metrics to incorporate advanced analytics like Expected Goals (xG) and Shot-Creating Actions (SCA). These statistics are fed into a pre-trained Random Forest model to generate an objective performance score.

## Core Features
*   **Positional Evaluation Logic:** Dynamically renders context-specific metrics on the frontend, highlighting ball progression for midfielders and defensive actions for center-backs.
*   **Historical Season Toggling:** Allows users to seamlessly switch between the 2023/2024 and 2024/2025 campaigns to track performance trends over time. *Note on season availability:* The application focuses on these specific historical campaigns because the free tier of the API-Football service intentionally restricts access to the most current, ongoing season.
*   **In-Memory Caching (Rate Limit Protection):** Utilizes Spring Cache with strict null-safety protocols to optimize data retrieval, minimize load times, and protect strict API rate limits. Because the project relies on free-tier APIs (which cap usage at roughly 100 requests per day and 10 requests per minute), this caching layer is critical to prevent the application from being temporarily blocked by the provider's firewall during periods of high traffic.
*   **Embedded Machine Learning Inference:** Evaluates players instantly without requiring external AI microservices or Python dependencies.

## Machine Learning Pipeline (ONNX)
A core technical achievement of this project is the integration of **ONNX (Open Neural Network Exchange)**.

Typically, machine learning models are trained and executed in Python, which often forces Java-based applications to rely on slow REST APIs to get predictions. By using ONNX, this architecture solves that bottleneck:
1.  **Training:** The Random Forest algorithm was trained externally using Python (scikit-learn) on historical Ballon d'Or data.
2.  **Exporting:** The trained model was serialized into a platform-agnostic `.onnx` file format.
3.  **Inference:** The Spring Boot backend uses **Microsoft's ONNX Runtime** for Java to load the `.onnx` file directly into memory.

This allows the application to map the aggregated football statistics into ONNX Tensors and execute the AI predictions natively within the JVM. The result is ultra-low latency scoring (<10ms per player) and a simplified deployment architecture that does not require a Python environment in production.

## Technology Stack & Data Providers
*   **Backend Framework:** Java, Spring Boot
*   **AI/ML Execution:** ONNX Runtime for Java
*   **Frontend Interface:** Thymeleaf, Tailwind CSS
*   **Data Providers & Limitations:** 
    *   **FBref (Training Dataset):** The Random Forest model was trained using free, publicly available CSV datasets from FBref. Because this data is statically exported from their public repository rather than fetched live, it represents a fixed historical snapshot of player performance.
    *   **API-Football via RapidAPI (Live Inference):** The application fetches match statistics using the free tier of API-Football. 
    *   **Why older seasons are used:** The free subscription explicitly limits the available seasons, locking the most current live data behind a premium paywall and limiting free users to complete historical seasons (like 23/24). Furthermore, the hard rate limits of the free tier dictate a heavy reliance on the cached historical data rather than querying the provider on every page load.

## Local Setup Configuration
To run this application locally, you must provide your own RapidAPI credentials via environment variables to ensure sensitive data remains out of version control.

1.  Clone the repository to your local development environment.
2.  Configure your IDE or terminal to inject the `API_FOOTBALL_KEY` environment variable.
3.  Build and run the Spring Boot application.
4.  Navigate to `http://localhost:8080` to view the dynamically generated leaderboard.
