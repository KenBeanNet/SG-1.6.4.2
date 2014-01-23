package mods.scourgecraft.helpers.point;

import java.io.Serializable;

import net.minecraft.entity.Entity;

public class Point implements Comparable<Point>
{
        /**
         *
         */

        public int                                        x;

        public int                                        y;

        public int                                        z;

        public Point(int x, int y, int z)
        {
                this.x = x;
                this.y = y;
                this.z = z;
        }

        public Point(Entity player)
        {
                x = (int) Math.floor(player.posX);
                y = (int) Math.floor(player.posY);
                z = (int) Math.floor(player.posZ);
        }

        /**
         * This is calculated by the whichever has higher coords.
         * @return Posotive number if this Point is larger. 0 if they are equal.
         * Negative if the provided point is larger.
         */
        @Override
        public int compareTo(Point point)
        {
                if (equals(point))
                        return 0;

                int positives = 0;
                int negatives = 0;

                if (x > point.x)
                {
                        positives++;
                }
                else
                {
                        negatives++;
                }

                if (y > point.y)
                {
                        positives++;
                }
                else
                {
                        negatives++;
                }

                if (z > point.z)
                {
                        positives++;
                }
                else
                {
                        negatives++;
                }

                if (positives > negatives)
                        return +1;
                else if (negatives > positives)
                        return -1;
                else
                        return x - point.x + y - point.y + z - point.z;
        }

        @Override
        public boolean equals(Object object)
        {
                if (object instanceof Point && x == ((Point) object).x && y == ((Point) object).y && z == ((Point) object).z)
                        return true;

                return false;
        }

        /**
         * @param point
         * @return The distance to a given Block.
         */
        public double getDistanceTo(Point point)
        {
                return Math.sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y) + (z - point.z) * (z - point.z));
        }

        /**
         * @param p
         * @return TRUE if the points have the same coordinate on at least one axis.
         */
        public boolean alignsWith(Point p)
        {
                return x == p.x || y == p.y || z == p.z;
        }

        public boolean isGreaterThan(Point p)
        {
                if (equals(p))
                        return false;

                return x >= p.x && y >= p.y && z >= p.z;
        }

        public boolean isLessThan(Point p)
        {
                if (equals(p))
                        return false;

                return x <= p.x && y <= p.y && z <= p.z;
        }

        /**
         * gets a new Point with the same data as the provided one.
         * @param point
         * @return
         */
        public static Point copy(Point point)
        {
                return new Point(point.x, point.y, point.z);
        }

        /**
         * ensures the Point is valid. Just floors the Y axis to 0. Y can't be
         * negative.
         */
        public void validate()
        {
                if (y < 0)
                {
                        y = 0;
                }
        }

        @Override
        public String toString()
        {
                return "Point[" + x + ", " + y + ", " + z + "]";
        }
}