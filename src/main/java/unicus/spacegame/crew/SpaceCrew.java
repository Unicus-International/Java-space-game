package unicus.spacegame.crew;
import org.apache.commons.lang3.ArrayUtils;
import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.MainBridge;
import unicus.spacegame.spaceship.cunstruction.Construction;
import unicus.spacegame.ui.DebugConsole;
import unicus.spacegame.utilities.ObjectKey;
import static unicus.spacegame.utilities.Constants.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
/*
 * Refactor notes:
 * Crew.java is renamed to SpaceCrew.java.
 * Package spacegame.crew has been added as a place to keep all crew code
 * SpaceCrew is going to be the main model class for crew
 *   in the same way HomeShip holds the model for the home-ship.
 * */

public class SpaceCrew {

    private static SpaceCrew SC;
    public static SpaceCrew SC() {
        if (SC == null)
            new SpaceCrew(); //constructor sets instance.
        return SC;
    }

    private final ObjectKey crewKeys;
    private final ObjectKey jobKeys;
    private final ObjectKey housingKeys;

    /**
     * List of all crewman objects that can be referenced in game, living or dead.
     * All lists and references of crewmen eventually refer to this list.
     */
    private AbstractCrewman[] crewmen;
    /**
     * Lists of all job objects that can be referenced in game, active or not.
     * All lists and references to jobs eventually refer to this list.
     */
    private AbstractJob[] jobs;
    private JobAssignment[] jobAssignments;

    private AbstractHousing[] housings;
    private HousingAssignment[] housingAssignments;

    private SpaceCrew(){
        this.crewmen = new AbstractCrewman[0];
        this.jobs = new AbstractJob[0];
        this.jobAssignments = new JobAssignment[0];
        this.housings = new AbstractHousing[0];
        this.housingAssignments = new HousingAssignment[0];
        SC = this;

        jobKeys = new ObjectKey();
        crewKeys = new ObjectKey();
        housingKeys = new ObjectKey();

        //set reserved keys
        jobKeys.setReserved(
                CONSTRUCTION_JOB_KEY,
                CAPTAIN_JOB_KEY,
                BRIDGE_JOB_KEY,
                MAIN_ENGINEER_JOB_KEY
        );
        DebugConsole.getInstance().addCrewCommands();
    }
    public void endOfMonthJobsHousing(){
        int i;
        for (i = jobAssignments.length - 1; i >= 0; i--) {
            JobAssignment ja = jobAssignments[i];
            ja.endOfMonth();
        }
        for (i = jobs.length - 1; i >= 0; i--) {
            AbstractJob job = jobs[i];
            job.endOfMonth();
        }
        for (i = housings.length - 1; i >= 0; i--) {
            AbstractHousing housing = housings[i];
            housing.endOfMonth();
        }
    }
    public void endOfMonthCrew(){
        for (int i = crewmen.length - 1; i >= 0; i--) {
            AbstractCrewman crewman = crewmen[i];
            crewman.endOfMonth();
        }
    }


    //TODO: crewGenerator (start scenarios), crew-lists

    /*
    Note: consider replacing array with hash map of key ids
     */


    //STUB!
    public static SpaceCrew GenerateStart1() {
        SpaceCrew crew = SC();

        /*
        Starting crew, made mostly at random. Feel free to change minor details
        NOTE: There should be more crewman onboard, but this is the start for now.


        4 male
        Hugh Frost - 381 months old, Bridge crew (captain)
        Zach Frost - 360 months old, Maintenance (logistics expert)
        Cole Rowe - 570 months old, passenger (asteroid miner)
        George Hawkins - 312 months old, passenger (cook)

        6 female
        Eden Day - 408 months old, engineer
        Rosie Connor - 308 months old, Bridge crew (pilot expert)
        Jessie Marshall - 400 months old, Bridge crew (navigation expert)
        Ciara Palmer - 302 months old, engineer
        Alicia Hatcher - 320 months old, passenger (asteroid miner)
        Norma López - 432 months old, passenger (security / combat expert)

         */
        Random r = new Random(0);
        SkillSet skills;

        //#region crew initialization
        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.socialization, SkillType.navigation);
        AdultCrewman Hugh = new AdultCrewman(crew.crewKeys.yieldKey(),
                -381, new CrewSelfID("Hugh Frost", CrewGender.male), new CrewmanGeneData(),
                skills,60, 6000);

        skills = new SkillSet(r, 50, 20, 10, 50, SkillType.artifice, SkillType.doctoring);
        AdultCrewman Zach = new AdultCrewman(crew.crewKeys.yieldKey(),
                -360, new CrewSelfID("Zach Frost", CrewGender.male), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 30, SkillType.combat, SkillType.weaponry);
        AdultCrewman Cole = new AdultCrewman(crew.crewKeys.yieldKey(),
                -570, new CrewSelfID("Cole Rowe", CrewGender.male), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.socialization, SkillType.doctoring);
        AdultCrewman George = new AdultCrewman(crew.crewKeys.yieldKey(),
                -312, new CrewSelfID("George Hawkins", CrewGender.male), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.artifice);
        AdultCrewman Eden = new AdultCrewman(crew.crewKeys.yieldKey(),
                -408, new CrewSelfID("Eden Day", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.navigation);
        AdultCrewman Rosie = new AdultCrewman(crew.crewKeys.yieldKey(),
                -308, new CrewSelfID("Rosie Connor", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 30, SkillType.navigation);
        AdultCrewman Jessie = new AdultCrewman(crew.crewKeys.yieldKey(),
                -400, new CrewSelfID("Jessie Marshall", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.artifice);
        AdultCrewman Ciara = new AdultCrewman(crew.crewKeys.yieldKey(),
                -302, new CrewSelfID("Ciara Palmer", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.weaponry);
        AdultCrewman Alicia = new AdultCrewman(crew.crewKeys.yieldKey(),
                -320, new CrewSelfID("Alicia Hatcher", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );

        skills = new SkillSet(r, 60, 20, 10, 50, SkillType.socialization, SkillType.combat);
        AdultCrewman Norma = new AdultCrewman(crew.crewKeys.yieldKey(),
                -432, new CrewSelfID("Norma López", CrewGender.female), new CrewmanGeneData(),
                skills, 60, 6000 );
        //#endregion
        crew.addReplaceCrewmen(Hugh, Zach, Cole, George, Eden, Rosie, Jessie, Ciara, Alicia, Norma);

        //assign captain.
        crew.assignJobCrew(CAPTAIN_JOB_KEY, Hugh.keyID);

        //Assign bridge crew. (note: crewmen may have multiple jobs)
        crew.assignJobCrew(BRIDGE_JOB_KEY, Hugh.keyID);
        crew.assignJobCrew(BRIDGE_JOB_KEY, Rosie.keyID);
        crew.assignJobCrew(BRIDGE_JOB_KEY, Jessie.keyID);

        //Assign Engineering job
        crew.assignJobCrew(MAIN_ENGINEER_JOB_KEY, Zach.keyID);
        crew.assignJobCrew(MAIN_ENGINEER_JOB_KEY, Eden.keyID);
        crew.assignJobCrew(MAIN_ENGINEER_JOB_KEY, Ciara.keyID);

        return crew;
    }

    public AbstractJob getJob(int jobID){
        for (AbstractJob j : jobs) {
            if(j.getKeyID() == jobID)
                return j;
        }
        return null;
    }
    public AbstractCrewman getCrew(int crewID){
        for (AbstractCrewman c : crewmen) {
            if(c.getKeyID() == crewID)
                return c;
        }
        return null;
    }
    public AbstractHousing getHousing(int housingID){
        for (AbstractHousing h : housings) {
            if(h.getKeyID() == housingID) {
                return h;
            }
        }
        return null;
    }

    /**
     * Get a list of all unemployed able crewmen.
     * @return
     */
    public AbleCrewman[] getUnemployed() {
        ArrayList<AbleCrewman> unemployed = new ArrayList<>();
        for (AbstractCrewman crewman : crewmen) {
            if(!crewman.getState().isWorkAble())
                continue;
            if(getJobAssignmentsByCrewman(crewman.keyID).length <= 0)
                unemployed.add((AbleCrewman) crewman);
        }
        return unemployed.toArray(new AbleCrewman[0]);
    }


    /**
     * Adds new crewmen to the list of crewmen.
     * If a crewman already exists (same keyID), the old object will be replaced with the new.
     *
     * @param newCrewObjects
     */
    public void addReplaceCrewmen(AbstractCrewman... newCrewObjects) {
        int[] toRemove = new int[0];
        for (AbstractCrewman c:newCrewObjects) {
            for (int i = 0; i < crewmen.length; i++)
                if (crewmen[i].keyID == c.keyID) toRemove = ArrayUtils.add(toRemove, i);
        }
        crewmen = ArrayUtils.removeAll(crewmen, toRemove);
        crewmen = ArrayUtils.addAll(crewmen, newCrewObjects);
    }

    //NOTE: for now, removing crewmen should be considered impossible.
    //in-game, the last CrewState, memorial, may be considered the closest thing to 'removed'.

    //public void removeCrewmen(int... crewKeys) {
    //    int[] toRemove = new int[0];
    //    for (int key:crewKeys)
    //        for (int i = 0; i < crewmen.length; i++)
    //            if (crewmen[i].keyID == key) toRemove = ArrayUtils.add(toRemove, i);
    //    crewmen = ArrayUtils.removeAll(crewmen, toRemove);
    //
    //}
    /**
     * Adds new job to the list of jobs.
     * If a job already exists (same keyID), it should not, the old object will be replaced with the new.
     *
     * @param newJobObjects
     */
    public void addJobs(AbstractJob... newJobObjects) {
        int[] toRemove = new int[0];
        for (AbstractJob j:newJobObjects) {
            for (int i = 0; i < jobs.length; i++)
                if (jobs[i].getKeyID() == j.getKeyID()) toRemove = ArrayUtils.add(toRemove, i);
        }
        jobs = ArrayUtils.removeAll(jobs, toRemove);
        jobs = ArrayUtils.addAll(jobs, newJobObjects);
    }
    public void removeJobs(int... jobKeys) {
        int[] toRemove = new int[0];
        int i;
        for (i = 0; i < jobs.length; i++)
            if (ArrayUtils.contains(jobKeys, jobs[i].getKeyID()))
                toRemove = ArrayUtils.add(toRemove, i);
        jobs = ArrayUtils.removeAll(jobs, toRemove);
        toRemove = new int[0];
        for (i = 0; i < jobAssignments.length; i++)
            if(ArrayUtils.contains(jobKeys, jobAssignments[i].getJobID()))
                toRemove = ArrayUtils.add(toRemove, i);
        jobAssignments = ArrayUtils.removeAll(jobAssignments, toRemove);
    }

    public void addHousing(AbstractHousing... newHousingObjects) {
        int[] toRemove = new int[0];
        for (AbstractHousing h:newHousingObjects) {
            for (int i = 0; i < housings.length; i++)
                if (housings[i].getKeyID() == h.getKeyID()) toRemove = ArrayUtils.add(toRemove, i);
        }
        housings = ArrayUtils.removeAll(housings, toRemove);
        housings = ArrayUtils.addAll(housings, newHousingObjects);
    }

    public void removeHousing(int... housingKeys) {
        int[] toRemove = new int[0];
        int i;
        for (i = 0; i < housings.length; i++)
            if (ArrayUtils.contains(housingKeys, housings[i].getKeyID()))
                toRemove = ArrayUtils.add(toRemove, i);
        housings = ArrayUtils.removeAll(housings, toRemove);
        toRemove = new int[0];
        for (i = 0; i < housingAssignments.length; i++)
            if (ArrayUtils.contains(housingKeys, housingAssignments[i].getHousingID()))
                toRemove = ArrayUtils.add(toRemove, i);
        housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
    }


    public boolean canAssignJobCrew(int jobID, int crewID) {
        return canAssignJobCrew(jobID, crewID, new StringBuffer());
    }

    public boolean canAssignJobCrew(int jobID, int crewID, StringBuffer message) {
        AbstractJob job = getJob(jobID);
        AbleCrewman crewman = getAbleCrew(crewID);
        if(job == null) {
            message.append("Cannot assign crewman, invalid job ID.");
            return false;
        }
        if(crewman == null) {
            message.append("Cannot assign crewman, invalid crewman ID, or not able to work.");
            return false;
        }

        if(!job.crewmanAllowedJob(crewman, message))
            return false;
        int numAssigned = 0;
        for (JobAssignment a : jobAssignments) {
            if(a.getJobID() == jobID) {
                if(a.getCrewID() == crewID) {
                    message.append("Cannot assign crewman, crewman is already assigned.");
                    return false;
                }

                numAssigned ++;
                if(numAssigned >= job.getNumWorkerSlots()) {
                    message.append("Cannot assign crewman, the workplace is full.");
                    return false;
                }
            }
        }
        message.append("Crewman may be assigned.");
        return true;
    }

    private AbleCrewman getAbleCrew(int crewID) {
        AbstractCrewman crewman = getCrew(crewID);
        if(crewman.getState().isWorkAble())
            return (AbleCrewman) crewman;
        else
            return null;
    }

    public void assignJobCrew(int jobID, int crewID) {
        if(!canAssignJobCrew(jobID, crewID))
            return;
        JobAssignment newJA = new JobAssignment(jobID, crewID);
        jobAssignments = ArrayUtils.add(jobAssignments, newJA);
    }

    public void unassignJobCrew(int jobID, int crewID) {
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID && jobAssignments[i].getCrewID() == crewID) {
                jobAssignments = ArrayUtils.remove(jobAssignments, i);
                return;
            }
        }
    }

    public void unassignAllJobCrew(int jobID) {
        int[] toRemove = new int[0];
        for (int i = 0; i < jobAssignments.length; i++) {
            if(jobAssignments[i].getJobID() == jobID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
        }
        jobAssignments = ArrayUtils.removeAll(jobAssignments, toRemove);
    }

    public JobAssignment[] getJobAssignmentsByJob(int jobID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;

    }
    public JobAssignment[] getJobAssignmentsByCrewman(int crewID){
        JobAssignment[] assignments = new JobAssignment[0];
        for (JobAssignment ja : jobAssignments) {
            if(ja.getCrewID() == crewID)
                assignments = ArrayUtils.add(assignments, ja);
        }
        return assignments;
    }
    public JobAssignment getJobAssignment(int jobID, int crewID) {
        for (JobAssignment ja : jobAssignments) {
            if(ja.getJobID() == jobID && ja.getCrewID() == crewID)
                return ja;
        }
        return null;
    }

    public boolean canAssignHouseCrew(int housingID, int crewID) {
        return canAssignHouseCrew(housingID, crewID, new StringBuffer());
    }
    public boolean canAssignHouseCrew(int housingID, int crewID, StringBuffer message) {
        AbstractHousing h = getHousing(housingID);
        AbstractCrewman c = getCrew(crewID);

        if(h == null) {
            message.append("Invalid housing selection");
            return false;
        }
        if(c == null) {
            message.append("Invalid crewman selection");
            return false;
        }
        if(c.getState() == CrewmanState.corpse || c.getState() == CrewmanState.memorial) {
            message.append("This crewman is dead. It simply would not be proper.");
            return false;
        }
        if(getHouseAssignment(housingID, crewID) != null) {
            message.append("This crewman already lives here.");
            return false;
        }
        int numResidents = getResidentsOfHouse(housingID).length;
        if(numResidents >= h.getCapacity()) {
            message.append("The house is full of people already!");
            return false;
        }
        message.append("The crewman can move here");
        return true;
    }

    public void assignHousingCrew(int housingID, int crewID, boolean force) {
        if(force || canAssignHouseCrew(housingID, crewID)){
            evictCrewman(crewID); //remove crewman from any previous housing
           HousingAssignment newHA = new HousingAssignment(housingID, crewID);
           housingAssignments = ArrayUtils.add(housingAssignments, newHA);
        }
    }

    public void evictCrewman(int crewID){
        int[] toRemove = new int[0];
        for (int i = 0; i < housingAssignments.length; i++) {
            if(housingAssignments[i].getCrewID() == crewID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
            housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
        }
    }

    public void evictAllFromHousing(int housingID) {
        int[] toRemove = new int[0];
        for (int i = 0; i < housingAssignments.length; i++) {
            if(housingAssignments[i].getHousingID() == housingID) {
                toRemove = ArrayUtils.add(toRemove, i);
            }
            housingAssignments = ArrayUtils.removeAll(housingAssignments, toRemove);
        }
    }

    public HousingAssignment[] getResidentsOfHouse(int housingID){
        HousingAssignment[] assignments = new HousingAssignment[0];
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getHousingID() == housingID)
                assignments = ArrayUtils.add(assignments, ha);
        }
        return assignments;
    }

    public HousingAssignment getHousingByCrew(int crewID){
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getCrewID() == crewID)
                return ha;
        }
        return null; //home
    }

    public HousingAssignment getHouseAssignment(int housingID, int crewID) {
        for (HousingAssignment ha : housingAssignments) {
            if(ha.getHousingID() == housingID && ha.getCrewID() == crewID)
                return ha;
        }
        return null;
    }

    public AbstractCrewman[] getCrewmen() {
        return crewmen;
    }
    public AbstractJob[] getJobs() {
        return jobs;
    }

    public ObjectKey getCrewKeys() {
        return crewKeys;
    }
    public ObjectKey getJobKeys() {
        return jobKeys;
    }
    public ObjectKey getHousingKeys() {
        return housingKeys;
    }

}