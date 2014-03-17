package de.minetick;

public class PlayerMovement {

    private int centerX;
    private int centerZ;
    private int movementX;
    private int movementZ;

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

    public void addMovement(PlayerMovement previousmovement, boolean updateCenter) {
        this.movementX += previousmovement.getMovementX();
        this.movementZ += previousmovement.getMovementZ();
        if(updateCenter) {
            this.centerX = previousmovement.getCenterX();
            this.centerZ = previousmovement.getCenterZ();
        }
    }

    public int getMovementX() {
        return this.movementX;
    }

    public int getMovementZ() {
        return this.movementZ;
    }

    public int getCenterX() {
        return this.centerX;
    }

    public int getCenterZ() {
        return this.centerZ;
    }
}
