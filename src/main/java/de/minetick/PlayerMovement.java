package de.minetick;

public class PlayerMovement {

    public final int centerX;
    public final int centerZ;
    public final int movementX;
    public final int movementZ;

    public PlayerMovement(int centerX, int centerZ, int movementX, int movementZ) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.movementX = movementX;
        this.movementZ = movementZ;
    }

    public PlayerMovement(int[] center, int[] movement) {
        this.centerX = center[0];
        this.centerZ = center[1];
        this.movementX = movement[0];
        this.movementZ = movement[1];
    }
}
