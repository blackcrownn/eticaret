package com.eticaret.backend.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing category.
 */
public class UpdateCategoryRequest {

        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        private String name;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        private Boolean active;

        public UpdateCategoryRequest() {
        }

        public UpdateCategoryRequest(String name, String description, Boolean active) {
                this.name = name;
                this.description = description;
                this.active = active;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Boolean getActive() {
                return active;
        }

        public void setActive(Boolean active) {
                this.active = active;
        }
}
