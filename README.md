<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<h1 align="center">Rate Limiter API</h1>
<h3 align="center">Redis-Based Token Bucket Rate Limiting</h3>

<p align="center">
A production-ready rate limiting API built using Spring Boot, Redis, and the Token Bucket algorithm
</p>

<hr>

<h2>📜 Table of Contents</h2>
<ul>
    <li><a href="#-project-overview">📌 Project Overview</a></li>
    <li><a href="#-features">✨ Features</a></li>
    <li><a href="#-architecture">🏗️ Architecture</a></li>
    <li><a href="#-api-endpoints">🔗 API Endpoints</a></li>
    <li><a href="#-rate-limiting-strategy">🧠 Rate Limiting Strategy</a></li>
    <li><a href="#-configuration">⚙️ Configuration</a></li>
    <li><a href="#-how-to-run">▶️ How to Run</a></li>
    <li><a href="#-testing">🧪 Testing</a></li>
    <li><a href="#-technologies">🛠️ Technologies</a></li>
    <li><a href="#-audience">🎓 Who Is This For?</a></li>
    <li><a href="#-future">🚀 Future Enhancements</a></li>
</ul>

<hr>

<h2 id="project-overview">📌 Project Overview</h2>

<p>
This project implements a <strong>Redis-backed Rate Limiter API</strong> using the
<strong>Token Bucket algorithm</strong>.
It protects backend APIs from excessive traffic by limiting the number of requests
a client can make within a given time window.
</p>

<p>
The project is designed with <strong>real production considerations</strong> such as:
</p>

<ul>
    <li>Distributed rate limiting using Redis</li>
    <li>Non-blocking reactive APIs</li>
    <li>Clear separation between monitoring and enforcement</li>
    <li>Proper HTTP status codes (<code>429 Too Many Requests</code>)</li>
</ul>

<hr>

<h2 id="features">✨ Features</h2>

<ul>
    <li>Token Bucket rate limiting algorithm</li>
    <li>Redis-based distributed token storage</li>
    <li>Configurable capacity and refill rate</li>
    <li>Per-client rate limiting using client IP</li>
    <li>Monitoring endpoint for token status</li>
    <li>Proper error handling with HTTP 429</li>
    <li>Production-ready architecture</li>
</ul>

<hr>

<h2 id="architecture">🏗️ Architecture</h2>

<pre>
Client Request
  |
  v
Spring Boot Controller
  |
  v
RateLimiterService (Token Check)
  |
  v
RedisTokenBucketService
  |
  v
Redis (Distributed Storage)
</pre>

<p>
<strong>Key principle:</strong><br>
Monitoring endpoints are <em>read-only</em> and do NOT mutate Redis state.
Only business APIs consume tokens.
</p>

<hr>

<h2 id="api-endpoints">🔗 API Endpoints</h2>

<h3>1️⃣ Status Endpoint (No Rate Limit)</h3>

<pre>
GET /rate-limiter/status
</pre>

<!-- Continue with all other sections... -->

<h2 id="configuration">⚙️ Configuration</h2>
<!-- content -->

<h2 id="how-to-run">▶️ How to Run</h2>
<!-- content -->

<h2 id="testing">🧪 Testing</h2>
<!-- content -->

<h2 id="technologies">🛠️ Technologies Used</h2>
<!-- content -->

<h2 id="audience">🎓 Who Is This For?</h2>
<!-- content -->

<h2 id="future">🚀 Future Enhancements</h2>
<!-- content -->

</body>
</html>
