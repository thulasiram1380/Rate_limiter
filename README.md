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
    <li><a href="#project-overview">📌 Project Overview</a></li>
    <li><a href="#features">✨ Features</a></li>
    <li><a href="#architecture">🏗️ Architecture</a></li>
    <li><a href="#api-endpoints">🔗 API Endpoints</a></li>
    <li><a href="#rate-limiting-strategy">🧠 Rate Limiting Strategy</a></li>
    <li><a href="#configuration">⚙️ Configuration</a></li>
    <li><a href="#testing">🧪 Testing</a></li>
    <li><a href="#technologies">🛠️ Technologies Used</a></li>
</ul>

<hr>

<h2 id="project-overview">📌 Project Overview</h2>

<p>
This project implements a <strong>Redis-backed Rate Limiter API</strong> using the
<strong>Token Bucket algorithm</strong>.
It protects backend APIs from excessive traffic by limiting the number of requests
a client can make within a given time window.
</p>

<ul>
    <li>Distributed rate limiting using Redis</li>
    <li>Non-blocking Spring Boot APIs</li>
    <li>Clear separation between monitoring and enforcement</li>
    <li>Proper HTTP status codes (<code>429 Too Many Requests</code>)</li>
</ul>

<hr>

<h2 id="features">✨ Features</h2>

<ul>
    <li>Token Bucket rate limiting algorithm</li>
    <li>Redis-based distributed token storage</li>
    <li>Configurable request capacity and refill rate</li>
    <li>Per-client rate limiting using client IP</li>
    <li>Read-only monitoring endpoint</li>
    <li>HTTP 429 error handling for rate limit violations</li>
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
RateLimiterService
  |
  v
RedisTokenBucketService
  |
  v
Redis (Distributed Storage)
</pre>

<p>
<strong>Design principle:</strong><br>
Monitoring endpoints are <em>read-only</em> and never mutate Redis state.
Only rate-limited APIs consume tokens.
</p>

<hr>

<h2 id="api-endpoints">🔗 API Endpoints</h2>

<h3>1️⃣ Status Endpoint (No Rate Limit)</h3>

<pre>
GET /rate-limiter/status
</pre>

<p>
Returns current token status without consuming tokens.
</p>

<pre>
{
  "status": "UP",
  "service": "rate-limiting-gateway",
  "clientId": "0:0:0:0:0:0:0:1",
  "capacity": 5,
  "availableTokens": 5
}
</pre>

<hr>

<h3>2️⃣ Rate-Limited API</h3>

<pre>
GET /api/test
</pre>

<p>
Consumes one token per request.
</p>

<p><strong>Success Response</strong></p>

<pre>
{
  "status": 200,
  "message": "Request allowed",
  "clientId": "0:0:0:0:0:0:0:1"
}
</pre>

<p><strong>Error Response</strong></p>

<pre>
{
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded",
  "clientId": "0:0:0:0:0:0:0:1"
}
</pre>

<hr>

<h2 id="rate-limiting-strategy">🧠 Rate Limiting Strategy</h2>

<ul>
    <li>Each client has a fixed-size token bucket</li>
    <li>Each request consumes one token</li>
    <li>Tokens refill based on configured refill rate</li>
    <li>Requests are blocked when tokens reach zero</li>
</ul>

<hr>

<h2 id="configuration">⚙️ Configuration</h2>

<p><strong>application.properties</strong></p>

<pre>
spring.application.name=Rate_Limiter_API
spring.cloud.gateway.discovery.locator.enabled=true

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000

rate-limiter.capacity=5
rate-limiter.refill-rate=0
rate-limiter.api-server-url=http://localhost:8081
rate-limiter.timeout=5000
</pre>

<p>
<strong>Note:</strong> Setting <code>refill-rate=0</code> helps demonstrate
rate limiting behavior clearly by triggering <code>429</code> responses after
the configured capacity is exhausted.
</p>

<hr>

<h2 id="testing">🧪 Testing</h2>

<p><strong>Using IntelliJ HTTP Client</strong></p>

<pre>
{% for i in range(1, 6) %}
###
GET http://localhost:8080/api/test
{% endfor %}
</pre>

<p>
After 5 requests, further calls will return <code>429 Too Many Requests</code>.
</p>

<hr>

<h2 id="technologies">🛠️ Technologies Used</h2>

<ul>
    <li>Java</li>
    <li>Spring Boot</li>
    <li>Redis</li>
    <li>Jedis (Redis client)</li>
    <li>Token Bucket Algorithm</li>
    <li>Maven</li>
</ul>

<hr>

</body>
</html>
