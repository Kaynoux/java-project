package main;

/**
 * Tracks Errors and Successes while importing the different DB-Parts
 * 0 = successfull
 * 1 = missing information
 * 2 = duplicate
 * 3 = id does not belong to an object
 */
class DbPartStats
{
    public int[] errors = new int[4];
}