package ru.kata.spring.boot_security.demo.DTO;

public class UserDto {

        private Long id;
        private String name;
        private String surname;
        private String age;
        private String email;
        private String password;
        private String[] rolesNames;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRolesNames() {
            return rolesNames;
        }

        public void setRolesNames(String[] rolesNames) {
            this.rolesNames = rolesNames;
        }

}
