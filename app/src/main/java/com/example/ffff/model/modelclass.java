package com.example.ffff.model;

public class modelclass {
    public class UserModel {
        private int id;
        private String email;
        private double money;
        private String transactionId;

        public UserModel(){}

        public UserModel(int id, String email, double money, String transactionId) {
            this.id = id;
            this.email = email;
            this.money = money;
            this.transactionId = transactionId;
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public double getMoney() {
            return money;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }

}
