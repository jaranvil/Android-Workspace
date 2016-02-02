package com.nscc.jared.gamejam;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jared on 1/30/2016.
 */
public class Room {
    // 1 - wall top
    // 2 - wall bottom
    // 3 - wall left
    // 4 - wall right
    // 5 - door

    protected int height;
    private int room[][] = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}};

    // 6 - puddle
    // 7 - jukebox
    // 8 - crate
    // 9 - pot
    // 10 - skelly
    private int[] optionalItems = {6,7,8,9,10};
    private int objectsInRoom[][] = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                                        {3,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,9,0,0,0,0,0,0,8,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,4},
                                        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {3,0,10,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}};

    protected int[][] layer1;
    protected int[][] layer2;

    public void generationRoom()
    {
        Random random = new Random();
        //int roomWidth = 15 + random.nextInt(25);
        //int roomHeight = 15 + random.nextInt(25);
        int roomWidth = 20;
        int roomHeight = 30;

        this.height = roomHeight;
        createLayer1(roomWidth, roomHeight);
        createLayer2(roomWidth, roomHeight);
    }

    // background tiles
    public void createLayer1(int roomWidth, int roomHeight)
    {
        layer1 = new int[roomWidth][roomHeight];

        for (int row = 0;row < layer1.length;row++)
            for (int col = 0;col < layer1[row].length;col++)
            {
                if (row == 0)
                    layer1[row][col] = 1;
                if (col == 0)
                    layer1[row][col] = 3;
                if (row == roomWidth -1)
                    layer1[row][col] = 2;
                if (col == roomHeight -1)
                    layer1[row][col] = 4;
            }

        // add door
        int middleRow = roomWidth/2;
        layer1[middleRow-1][roomHeight-1] = 5;
        layer1[middleRow][roomHeight-1] = 5;
        layer1[middleRow+1][roomHeight-1] = 5;

        layer1[middleRow-1][0] = 5;
        layer1[middleRow][0] = 5;
        layer1[middleRow+1][0] = 5;

        int middleCol = roomHeight/2;
        layer1[0][middleCol-1] = 5;
        layer1[0][middleCol] = 5;
        layer1[0][middleCol+1] = 5;

        layer1[roomWidth-1][middleCol-1] = 5;
        layer1[roomWidth-1][middleCol] = 5;
        layer1[roomWidth-1][middleCol+1] = 5;
    }

    public void createLayer2(int roomWidth, int roomHeight)
    {
        layer2 = new int[roomWidth][roomHeight];
        Random random = new Random();
        int numberOfItems = 2 + random.nextInt(3);
        ArrayList<Integer> addedObects = new ArrayList<>();

        int counter = 0;
        boolean stop = false;
        for (int row = 0;row < layer2.length;row++)
            for (int col = 0;col < layer2[row].length;col++)
            {
                if (!stop)
                {
                    // add item
                    int colPos = 3 + random.nextInt(roomHeight-4);
                    int rowPos = 3 + random.nextInt(roomWidth-4);

                    int item = random.nextInt(optionalItems.length);

                    if (!addedObects.contains(item))
                    {
                        layer2[rowPos][colPos] = optionalItems[item];
                        addedObects.add(item);
                    }

                    counter++;

                    if (counter == numberOfItems)
                        stop = true;
                }

            }
    }


}
