# Food Ordering Platform (Microservices Architecture)

A scalable food ordering platform built with a **microservices architecture**, supporting user sign-up, login, restaurant browsing, order placement, payments, and rider delivery assignment.  

---

## 🚀 Features

- **User Authentication**: Signup & Login with JWT-based authentication  
- **Restaurant Discovery**: Browse nearby restaurants (filter by cuisine, rating, food items)  
- **Menu & Cart**: View menus, add/remove items from cart, and place orders  
- **Order Orchestration**: Real-time order processing, restaurant confirmation, rider assignment  
- **Payments**: Integration with third-party payment gateways  
- **Notifications**: SMS/Email for OTPs, order confirmations, and delivery updates  
- **Rider Management**: Riders can register and accept deliveries  

---

## 🏗️ Microservices Overview

| Service            | Responsibilities |
|--------------------|-----------------|
| **API Gateway**    | Request routing, load balancing, rate limiting |
| **Auth Service**   | Signup, Login, JWT issuance |
| **User Service**   | User profiles, addresses, carts |
| **Restaurant Service** | Restaurant registration, menu management |
| **Order Service**  | Orchestrates end-to-end order lifecycle |
| **Payment Service**| Handles payments via third-party gateway |
| **Notification Service** | Sends OTPs, order updates |
| **Rider Service**  | Rider registration, availability, assignment |

---

## 📡 Example API Endpoints

### Auth Service
- `POST /auth/signup` – User signup  
- `POST /auth/login` – User login, returns JWT  

### User Service
- `PUT /user/update` – Update user profile  
- `POST /user/cart/addItem` – Add item to cart  
- `DELETE /user/cart/removeItem` – Remove item from cart  
- `GET /user/cart` – Get current cart  

### Restaurant Service
- `GET /rest/view?lat={lat}&long={long}` – View nearby restaurants  
- `GET /rest/menu/{restId}` – Get restaurant menu  

### Order Service
- `POST /order/create` – Create a new order  
- `GET /order/status/{orderId}` – Track order status  

---

## 🗄️ Database Entities (High Level)

- **User Service**: `User`, `Address`, `Cart`, `CartItem`
- **Restaurant Service**: `Restaurant`, `MenuItem`
- **Order Service**: `Order`, `OrderItem`, `OrderStatus`
- **Rider Service**: `Rider`, `RiderLocation`, `DeliveryAssignment`
- **Payment Service**: `PaymentTransaction`

---

## 🔗 Communication & Dependencies

- **Synchronous (HTTP/gRPC)**:  
  - API Gateway → Auth/User/Restaurant/Order/Payment Services  
  - Order Service → Restaurant Service (order confirmation)  
  - Order Service → Payment Service (transaction)  

- **Asynchronous (Event-Driven / Messaging)**:  
  - `UserRegistered` event → User Service bootstrap profile  
  - `OrderPlaced` event → Notification Service (send confirmation)  
  - `OrderReady` event → Rider Service (assign nearest rider)  

---

## 📜 Sequence Diagram (Happy Flow)

```mermaid
sequenceDiagram
    participant User
    participant API
    participant Auth
    participant UserService
    participant Restaurant
    participant Order
    participant Payment
    participant Rider
    participant Notify

    User->>API: POST /auth/signup
    API->>Auth: Forward signup
    Auth-->>API: Success
    API-->>User: Signup OK

    User->>API: POST /auth/login
    API->>Auth: Forward login
    Auth-->>API: JWT
    API-->>User: JWT token

    User->>API: GET /rest/view?lat=..
    API->>Restaurant: Fetch nearby restaurants
    Restaurant-->>API: List of restaurants
    API-->>User: Restaurant list

    User->>API: POST /user/cart/addItem
    API->>UserService: Add item to cart
    UserService-->>API: OK

    User->>API: POST /order/create
    API->>Order: Create order
    Order->>Restaurant: Confirm availability
    Restaurant-->>Order: Confirmed
    Order->>Payment: Initiate payment
    Payment-->>Order: Payment success
    Order->>Rider: Assign delivery boy
    Rider-->>Order: Rider assigned
    Order->>Notify: Send confirmation
    Notify-->>User: Order placed notification
    Order-->>API: Order confirmed
    API-->>User: Order details & status
