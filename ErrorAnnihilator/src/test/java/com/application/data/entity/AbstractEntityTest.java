package com.application.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractEntityTest {

    private static class ConcreteEntity extends AbstractEntity {
        // Additional properties and methods for testing purposes
    }

    private static class OtherEntity extends AbstractEntity {
        // Additional properties and methods for testing purposes
    }

    @Test
    public void testGetSetId() {
        // Arrange
        AbstractEntity entity = new ConcreteEntity();
        Long id = 1L;

        // Act
        entity.setId(id);
        Long retrievedId = entity.getId();

        // Assert
        Assertions.assertEquals(id, retrievedId);
    }

    @Test
    public void testGetVersion() {
        // Arrange
        AbstractEntity entity = new ConcreteEntity();

        // Act
        int version = entity.getVersion();

        // Assert
        Assertions.assertEquals(0, version);
    }

    // method to return the hash code of the 'id'
    @Test
    public void testHashCode() {
        // Arrange
        AbstractEntity entity1 = new ConcreteEntity();
        AbstractEntity entity2 = new ConcreteEntity();
        Long id = 1L;
        entity1.setId(id);
        entity2.setId(id);

        // Act
        int hashCode1 = entity1.hashCode();
        int hashCode2 = entity2.hashCode();

        // Assert
        Assertions.assertEquals(hashCode1, hashCode2);
    }

    // method to compare objects based on 'id' field
    @Test
    public void testEquals() {
        // Arrange
        AbstractEntity entity1 = new ConcreteEntity();
        AbstractEntity entity2 = new ConcreteEntity();
        Long id1 = 1L;
        Long id2 = 2L;
        entity1.setId(id1);
        entity2.setId(id1);

        // Act
        boolean result1 = entity1.equals(entity2);
        boolean result2 = entity2.equals(entity1);

        // Assert
        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);

        // Arrange
        entity2.setId(id2);

        // Act
        boolean result3 = entity1.equals(entity2);
        boolean result4 = entity2.equals(entity1);

        // Assert
        Assertions.assertFalse(result3);
        Assertions.assertFalse(result4);

        // Arrange
        OtherEntity otherEntity = new OtherEntity();

        // Act
        boolean result5 = entity1.equals(otherEntity);

        // Assert
        Assertions.assertFalse(result5);
    }
}
