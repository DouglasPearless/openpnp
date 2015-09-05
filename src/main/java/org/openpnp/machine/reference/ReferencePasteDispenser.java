package org.openpnp.machine.reference;

import javax.swing.Action;

import org.openpnp.ConfigurationListener;
import org.openpnp.gui.support.PropertySheetWizardAdapter;
import org.openpnp.gui.support.Wizard;
import org.openpnp.model.Configuration;
import org.openpnp.model.Location;
import org.openpnp.spi.PropertySheetHolder;
import org.openpnp.spi.base.AbstractPasteDispenser;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferencePasteDispenser extends AbstractPasteDispenser implements
        ReferenceHeadMountable {
    private final static Logger logger = LoggerFactory
            .getLogger(ReferencePasteDispenser.class);

    @Element
    private Location headOffsets;

    @Attribute(required = false)
    private double safeZ = 0;
   
    
    protected ReferenceMachine machine;
    protected ReferenceDriver driver;

    public ReferencePasteDispenser() {
        Configuration.get().addListener(new ConfigurationListener.Adapter() {
            @Override
            public void configurationLoaded(Configuration configuration)
                    throws Exception {
                machine = (ReferenceMachine) configuration.getMachine();
                driver = machine.getDriver();
            }
        });
    }
    
    @Override
    public Location getHeadOffsets() {
        return headOffsets;
    }
    
    @Override
    public void setHeadOffsets(Location headOffsets) {
        this.headOffsets = headOffsets;
    }

    @Override
    public void dispense(Location startLocation, Location endLocation, long dispenseTimeMilliseconds) throws Exception {
		logger.debug("{}.dispense()", getName());
//		driver.dispense(this);
        machine.fireMachineHeadActivity(head);
    }

    @Override
    public void moveTo(Location location, double speed) throws Exception {
        logger.debug("{}.moveTo({}, {})", new Object[] { getName(), location, speed } );
        driver.moveTo(this, location, speed);
        machine.fireMachineHeadActivity(head);
    }

    @Override
    public void moveToSafeZ(double speed) throws Exception {
		logger.debug("{}.moveToSafeZ({})", new Object[]{getName(), speed});
        Location l = new Location(getLocation().getUnits(), Double.NaN,
                Double.NaN, safeZ, Double.NaN);
        driver.moveTo(this, l, speed);
        machine.fireMachineHeadActivity(head);
    }
    @Override
    public Location getLocation() {
        return driver.getLocation(this);
    }
    
    @Override
    public Wizard getConfigurationWizard() {
        return null;
    }
    
	@Override
    public String getPropertySheetHolderTitle() {
	    return getClass().getSimpleName() + " " + getName();
    }

    @Override
    public PropertySheetHolder[] getChildPropertySheetHolders() {
        return null;
    }

    @Override
    public PropertySheet[] getPropertySheets() {
        return new PropertySheet[] {
                new PropertySheetWizardAdapter(getConfigurationWizard())
        };
    }
        
    @Override
    public Action[] getPropertySheetHolderActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public String toString() {
		return getName();
	}
    
    public double getSafeZ() {
    	return safeZ;
    }
    
    public void setSafeZ(double safeZ) { 
    	this.safeZ = safeZ; 
    }

}