<div align="center">

# The Real d'Or

**AI-Powered Football Analytics & Player Ranking Dashboard**

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](#)
[![Spring_Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](#)
[![ONNX](https://img.shields.io/badge/ONNX-005CED?style=for-the-badge&logo=onnx&logoColor=white)](#)
[![Tailwind](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](#)

*A Computer Engineering thesis project by Tinotenda Ryan Ziko at Vistula University.*

---

<img src="docs/demo.gif" alt="Animated Application Demo" width="800" />
<br>
<em>(Tip: Record a short walkthrough of your UI and save it as <code>docs/demo.gif</code> to render the animation here.)</em>

</div>

## Overview

The Real d'Or is a data pipeline and presentation layer that connects sports statistics to predictive AI models. It aggregates match data to evaluate players based on on-pitch influence, utilizing metrics like Expected Goals (xG) and Shot-Creating Actions (SCA). These statistics feed into a pre-trained Random Forest model to output an objective performance score.

## System Architecture

The application handles data retrieval, caching, and machine learning inference entirely within the JVM, avoiding the need for external Python microservices.

```mermaid
graph TD
    A[API-Football / RapidAPI] -->|JSON Match Data| B(Spring Boot Backend)
    B -->|Miss| C[(Spring Cache)]
    C -->|Hit| B
    B -->|Feature Tensors| D{ONNX Runtime}
    D -->|RF Predictions| B
    B -->|Model Attributes| E[Thymeleaf + Tailwind]
    E --> F[Client Browser]
