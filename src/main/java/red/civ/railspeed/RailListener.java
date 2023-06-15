package red.civ.railspeed;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class RailListener implements Listener {

    static boolean debug=false;

    //set speed depending on block type
    @EventHandler
    public static void onMove(VehicleMoveEvent event){
        if(!(event.getVehicle() instanceof Minecart)){
            return;
        }
        Minecart minecart = (Minecart) event.getVehicle();
        if(minecart.getPassengers().size() < 1){
            minecart.setMaxSpeed(0.4);//typical minecraft max speed;
            return;
        }

        Material blocktype = minecart.getLocation().getBlock().getType();
        if(blocktype.equals(Material.RAIL) ||blocktype.equals(Material.POWERED_RAIL) || blocktype.equals(Material.ACTIVATOR_RAIL) || blocktype.equals(Material.POWERED_RAIL) ){
            minecart.setMaxSpeed(0.8);//default is 0.4
        }
        else{
            minecart.setMaxSpeed(0.4);//typical minecraft max speed;
        }

        if(debug){Logger.Warn("SPEED: " + String.valueOf(minecart.getMaxSpeed()));}
    }


    //slows speed to safe limits near curves
    @EventHandler
    public void preventDerail(VehicleMoveEvent event) {
        if(!(event.getVehicle() instanceof Minecart)){
            return;
        }
        Minecart minecart = (Minecart) event.getVehicle();

        if(minecart.getPassengers().size() < 1){
            return;
        }

        Vector v  = minecart.getVelocity();
        if(debug){Logger.Warn("Current velocity is " + minecart.getVelocity().toString());}

        if(upcomingcurve(minecart.getLocation().getBlock())){

            if(debug){Logger.Warn("MINECART ON CURVED RAIL!");}
            Vector cv = minecart.getVelocity();
            minecart.setVelocity(getsafevelocity(cv));
        }
    }

    //checks if the given rail shape is curved
    public boolean iscurved(Rail.Shape shape){
        if(shape.equals(Rail.Shape.NORTH_EAST)||shape.equals(Rail.Shape.NORTH_WEST)||shape.equals(Rail.Shape.SOUTH_EAST)||shape.equals(Rail.Shape.SOUTH_WEST)){
            return true;
        }
        return false;
    }

    //Looks for nearby curved rails 3x3 horizontial plane
    public boolean upcomingcurve(Block b){
        boolean upcomingcurve = false;
        Location central = null;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){

                central = b.getLocation();
                central.add(i,0,j);

                if(central.getBlock().getType().equals(Material.RAIL)){
                    Rail r =  (Rail) central.getBlock().getState().getBlockData();
                    if(iscurved(r.getShape())){
                        upcomingcurve=true;
                    }
                }
            }
        }
        return upcomingcurve;
    }

    public Vector getsafevelocity(Vector v){
        double safescalar = 0.70;//FINE TUNE

        double tosafescalar = 1.0;//DONT CHANGE
        //this finds the fraction required to scale the max value down to safescalar.

        double max = Math.max(v.getX(),v.getZ());//find which vector is highest, we will scale down the entire speed
        //based on how much the highest vector needs to come down to meet the
        if(max<safescalar){ return v; }

        tosafescalar = safescalar/max;

        if(v.getX()>safescalar){
            v.setX( tosafescalar * v.getX() );
        }
        if(v.getZ()>safescalar){
            v.setZ( tosafescalar * v.getZ() );
        }
        return v;
    }
}