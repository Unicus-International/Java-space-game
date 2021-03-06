package unicus.spacegame.spaceship;

/**
 * A datastructure that a Spaceship is full of.
 * Needs to know what ModuleType it is, and what SectionType it is hosted in.
 */
public abstract class AbstractShipModule extends AbstractShipPart{

    public AbstractShipModule(ShipLoc loc) {
        super(ShipPartType.Module, loc);
    }
    protected AbstractShipModule(ShipPartType partType, ShipLoc loc) {
        super(partType, loc);
    }
    public abstract int getNumComponents();
    public abstract abstractShipComponent[] getComponents();

    /**
     * For modules:
     * Whatever this module requires gravity to be constructed.
     * For section-frames:
     * Whatever this section-frame provides gravity.
     * @return
     */
    public abstract boolean useGravity();

    public abstract ModuleType getModuleType();

    /**
     * End of month update.
     * Modules that provides amenities must implement this.
     */
    public void endOfMonth() {

    }
}


