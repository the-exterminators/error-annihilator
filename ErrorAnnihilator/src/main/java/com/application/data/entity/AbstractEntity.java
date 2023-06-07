package com.application.data.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @SequenceGenerator(name = "idgenerator")
    private Long id;

    @Version
    // Used for optimistic locking to handle concurrent update
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    // method to return the hash code of the 'id'
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    // method to compare objects based on 'id' field
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity that)) {
            // null or not an AbstractEntity class
            return false;
        }
        if (getId() != null) {
            // If 'id' is not null, compare it with the 'id' of 'that'
            return getId().equals(that.getId());
        }
        // If 'id' is null, fall back to the superclass's equals method
        return super.equals(that);
    }
}
