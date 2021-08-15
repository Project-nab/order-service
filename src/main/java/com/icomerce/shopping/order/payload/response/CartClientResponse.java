package com.icomerce.shopping.order.payload.response;

import com.icomerce.shopping.order.entities.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartClientResponse {
    private int errorCode;
    private String message;
    private Cart result;

    public static class Cart {
        String sessionId;
        String username;
        Timestamp createdAt;
        Timestamp updatedAt;
        CartStatus cartStatus;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        public Timestamp getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
        }

        public CartStatus getCartStatus() {
            return cartStatus;
        }

        public void setCartStatus(CartStatus cartStatus) {
            this.cartStatus = cartStatus;
        }
    }
}
