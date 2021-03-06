package unicus.spacegame.crew;

/**
 * What is a job?
 * A job is a responsibility, a set of tasks, that needs to be done during each month.
 * A crewmembers needs to be assigned to a job for a job to function.
 *
 * Some jobs are temporary. There are two types of temporary jobs.
 *      * Recurring temporary jobs will be automatically disabled once the current task(s) are completed or aborted.
 *          This includes jobs like ship-repair, refit jobs, research, diplomacy etc.
 *          Jobs of this type are created at the start of a new game
 *          These jobs will be disabled when not in use.
 *      * Temporary jobs, spawned from events, eventchains and speacial needs.
 *          This includes jobs like guarding an alien prisoner, childcare, planetary expeditions etc.
 *          Jobs of this type are automatically created when needed, in case of childcare even automatically assigned.
 *          These jobs will be deleted once their use has been fullfilled.
 * Other jobs are set from the configuration of the ship, ie the modules constructed.
 *
 * A job's function varies, and so does the consequences for a job not being done.
 *
 * A list of jobs are stored in SpaceCrew
 * A list of job-assignments are stored in SpaceCrew
 * @see SpaceCrew
 *
 * Each job has a unique keyID
 *      The keyID is used to connect
 *          the job to workplace (if there is one)
 *          the crewmembers assigned to it.
 *      The keyID is always unique.
 *          If a duplicate keyID where to exist, the game may not function properly.
 * Each job have a maximum number of worker slots.
 *      This value is constant for a job, no matter the upgrades to the workplace.
 *      Note the worker slots need not be filled to reach full efficiency.
 * Each job has a workload to be shared between the workers.
 *      The workload may not be fixed.
 *      The workload is based on the amount of work expected to be done within the month.
 * Each job has a worker evaluation
 *       This is used to check how effeciently this worker *can* work.
 * Each job has a end-of-month report, where the work process is calculated.
 *      The final amount of work done is compared to the expected amount, providing effeciency data.
 *      Consequences for too little or too much work done varies between jobs, and are implemented here.
 * Implementation
 *      The implementation of AbstractJob may happen in a ship module or component as a inner or package-private class.
 *      However the implementation is made:
 *          A job-keyID must be obtained from SpaceCrew
 *          The job must be added to the list of jobs in SpaceCrew when an instance is created.
 *          The job must be removed from the list of jobs in SpaceCrew when an instance is removed.
 */

public abstract class AbstractJob {
    private final int keyID;
    private final int numWorkerSlots;
    private boolean active;

    protected double monthWorkDone;
    protected int monthWorstCrewman;
    protected int monthBestCrewman;
    protected JobAssignment[] monthJobAssignments;

    protected AbstractJob(int keyID, int numWorkerSlots) {
        this.keyID = keyID;
        this.numWorkerSlots = numWorkerSlots;
        active = true;
        monthWorkDone = 0;
    }

    public int getKeyID() {
        return keyID;
    }

    public int getNumWorkerSlots() {
        return numWorkerSlots;
    }

    /**
     * Gets the amount of workload to be put on assigned crewmembers this month.
     * @return A value of workload pressure.
     */
    public abstract double getMonthlyWorkload();

    /**
     * Calculates a base efficiency for how well a crewman will do this job.
     * Used in UI to show percentage efficiency.
     * Note: implementation should include the result from {@link AbleCrewman#getGeneralWorkModifier()},
     *      unless implementation has an alternative.
     *
     * @param crewID The ID of the crewman
     * @return The base efficiency of the crewman, where 1.0 equals 100%.
     *
     */
    public abstract double getWorkModifierOfCrewman(int crewID);
    /*TODO LATER: This method's implementation will be separate in each job, and depends a lot on the details of
     *implementations of both jobs and crew. It is important, but too early to write at this stage (Jan 2020).
     */

    /**
     * Completes work required for the month.
     * Completes task list and or operations.
     * May triggers events related to what has been worked on.
     *
     * planned feature:
     *         1. From each assignment of the job, get the total amount of work done.
     *         2. Calculate resulting product, operation,  service quality, and or amenities the job does.
     *             1. Consume resources required for the job.
     *             2. Reduce work done if resources are missing
     *
     */
    public void endOfMonth() {
        double max = 0;
        double min = Integer.MAX_VALUE;
        monthWorkDone = 0;
        monthJobAssignments = SpaceCrew.SC().getJobAssignmentsByJob(keyID);
        for (JobAssignment ja : monthJobAssignments) {
            int crewID = ja.getCrewID();
            if (ja.getWorkshare() == WorkShare.vacation)
                continue;
            double w = ja.getMonthWorkProduced();
            monthWorkDone += w;

            if (w > max) {
                max = w;
                monthBestCrewman = crewID;
            }
            if (w < min) {
                min = w;
                monthWorstCrewman = crewID;
            }
        }
        /*Leave it up the sub-classes on how to use
        monthBestCrewman, monthWorstCrewman and monthWorkDone
        */
    }

    /**
     * Returns false if there is any reason a crewman may not be assigned to this job.
     * If there is, an explanation must be appended to the message object.
     * If a crewman can take the job, but there are reasons why they should not, a warning should be appended.
     * @param crewman The crewman to potentially assign the job.
     * @param message The output area for UI feedback to user / player.
     * @return whatever the crewman is allowed to be assigned to this job.
     */
    public boolean crewmanAllowedJob(AbleCrewman crewman, StringBuffer message) {
        return true;
    }

    /**
     * Whatever this job is currently active.
     * Jobs can be disabled. No crew will work the job.
     * endOfMonth will still be called on a disabled job.
     * When a job is disabled, all assigned crewmen get unassigned from the job.
     * @return whatever the job is active.
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            //This unassigns all crewmen assigned to this job.
            SpaceCrew.SC().unassignAllJobCrew(keyID);
        }
    }

    /**
     * Total amount of work done by the end of the month.
     * Set in {@link #endOfMonth()}.
     * @return
     */
    public double getMonthWorkDone() {
        return monthWorkDone;
    }

    /**
     * Gets the crewman that did the least work this month (does not count vacation)
     * @return
     */
    public AbleCrewman getMonthWorstCrewman() {
        try {
            return (AbleCrewman) SpaceCrew.SC().getCrew(monthWorstCrewman);
        } catch (Exception err) {
            System.err.println(err);
            return null;
        }
    }

    /**
     * Gets the crewman that did the most work this month
     * @return
     */

    public AbleCrewman getMonthBestCrewman() {
        try {
            return (AbleCrewman) SpaceCrew.SC().getCrew(monthBestCrewman);
        } catch (Exception err) {
            System.err.println(err);
            return null;
        }
    }

    @Override
    public String toString() {
        return toString(new StringBuffer()).toString();
    }
    public StringBuffer toString(StringBuffer text) {
        text.append("Job ID: ").append(keyID).append("/n");
        text.append("Monthly workload: ").append(getMonthlyWorkload()).append("\n");
        JobAssignment[] workers = SpaceCrew.SC().getJobAssignmentsByJob(keyID);
        text.append("The job has ").append(workers.length).append(" assigned workers.\n");
        for (JobAssignment ja :
                workers) {
            text.append("\t*CrewID ").append(ja.getCrewID()).append(", assigned with a ").append(ja.getWorkshare()).append(" workshare.\n");
        }

        return text;
    }
}

