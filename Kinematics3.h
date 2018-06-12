
/*Purpose: This header declares a kinematic object used to 
 * model lab1 for Robotics, CS556. It contains constant values,
 * kinematic functions, mutator, and accessory methods.
 * AUTHOR: Melton, Connor
 * CLASS: Robotics, CS556
 * TEACHER: Vuskovic, Marko
 * ASSIGNMENT: LAB1
 * DUE: March 3rd, 2016
*/

#include <iostream>
#include <math.h>

   const double pi = 3.141592;
   const double d2r = pi/180.0;
   const double Rx = 100;
   const double Ry = 900;
   const double Sx = 1000;
   const double Sy = 200;
   const int N = 3;

class Kinematics3 {

private:

   double _a[N];   //Kinematic parameters (link lengths)
   double _pB[2];  //Position of the robot base

public:

   Kinematics3(double a1, double a2, double a3, double pB1, double pB2);
   void j2c (double q[], double p[], double *fi);
   void c2j (double q[], double p[], double *fi, double qinit[]);
   void setkin(double a[]);
   void setbase(double pB[]);
   void getpar (double a[], double pB[]);
   double myAbs(double x);

}; 

/* PURPOSE: Defines the methods described in Kinematics.h
 * AUTHOR: Melton, Connor
 * CLASS: Robotics, CS556
 * TEACHER: Vuskovic, Marko
 * ASSIGNMENT: LAB1
 * DUE: March 3rd, 2016
*/

// Constructor
Kinematics3::Kinematics3(double a1, double a2, double a3, double pB1, double pB2)
{
   _a[0] = a1;
   _a[1] = a2;
   _a[2] = a3;
   _pB[0] = pB1;
   _pB[1] = pB2;
}

/* Forward kinematic function: Takes robot's configuration, and calculates
 * all link poses in cartiesian space.
 * INPUTS
 * q[]: Robot configuration in degrees
 * p[]: Storage for link poses
 * *fi: N/A
 * NOTE: Pointer parameter specified in lab1 procedure seemed redundent
 * with an array reference (p[]), and so I decided not to use it in my
 * implementation of j2c.
 */
void Kinematics3::j2c(double q[], double p[], double *fi)
{
   double x1, x2, x3, y1, y2, y3, fi1, fi2, fi3;

//Finding link pose values

   fi1 = q[0];
   x1 = _pB[0] + _a[0] * cos(fi1 * d2r);
   y1 = _pB[1] + _a[0] * sin(fi1 * d2r);

   fi2 = fi1 + q[1];
   x2 = x1 + _a[1] * cos(fi2 * d2r);
   y2 = y1 + _a[1] * sin(fi2 * d2r);
   
   fi3 = fi2 + q[2];
   x3 = x2 + _a[2] * cos(fi3 * d2r);
   y3 = y2 + _a[2] * sin(fi3 * d2r);

//Populating P matrix

   p[0] = x1; p[1] = x2; p[2] = x3;
   p[3] = y1; p[4] = y2; p[5] = y3;
   p[6] = fi1; p[7] = fi2; p[8] = fi3;

}

/* Inverse kinematic function: Takes wrist pose of robot, and 
 * calculates the configuration with the "closest solution" discussed
 * in class.
 * INPUTS
 * q[]: Storage for robot configuration in degrees.
 * p[]: Wrist pose used to find q[].
 * qinit[]: Initial configuration before moving to given wrist pose p[].
 * *fi: N/A
 * NOTE: Pointer parameter specified in lab1 procedure seemed redundent
 * with an array reference (q[]), and so I decided not to use it in my
 * implementation of c2j.
 */
void Kinematics3::c2j(double q[], double p[], double *fi, double qinit[])
{
    
   double s1, s2, b1, b2, px, py, sum1, sum2;
   
   px = p[0] - _pB[0];
   py = p[1] - _pB[1];
   
// Initializes inverse kinematic values
   b1 = (pow(px, 2) + pow(py, 2) + pow(_a[0], 2) - pow(_a[1], 2))/(2 * _a[0]);
   b2 = (pow(px, 2) + pow(py, 2) + pow(_a[1], 2) - pow(_a[0], 2))/(2 * _a[1]);
   s1 = sqrt(pow(px, 2) + pow(py, 2) - pow(b1, 2));
   s2 = sqrt(pow(px, 2) + pow(py, 2) - pow(b2, 2));

// Calculates possible configuration
   double q1[3], q2[3];
   q1[0] = (atan2(py, px) - atan2(s1, b1)) / d2r;
   q1[1] = (atan2(s1, b1) + atan2(s2, b2)) / d2r;
   q1[2] = p[2] - q1[0] - q1[1];
   q2[0] = (atan2(py, px) + atan2(s1, b1)) / d2r;
   q2[1] = (-atan2(s1, b1) - atan2(s2, b2)) / d2r;
   q2[2] = p[2] - q2[0] - q2[1];

// Finds the closest solution
   for(int i = 0; i < 3; i++){
      sum1 = sum1 + myAbs(q1[i] - qinit[i]);
      sum2 = sum2 + myAbs(q2[i] - qinit[i]);
   }

   if(sum1 <= sum2){
       q[0] = q1[0];
       q[1] = q1[1];
       q[2] = q1[2];
   }
   else{
       q[0] = q2[0];
       q[1] = q2[1];
       q[2] = q2[2];
   }
}

// Mutator method for member _a[] (Link lengths)
void Kinematics3::setkin(double a[])
{
   _a[0] = a[0];
   _a[1] = a[1];
   _a[2] = a[2];
}

// Mutator method for member _pB[] (Robot base position)
void Kinematics3::setbase(double pB[])
{
   _pB[0] = pB[0];
   _pB[1] = pB[1];
}

// Accessory method for Kinematic3 members
void Kinematics3::getpar(double a[], double pB[])
{
   a[0] = _a[0];
   a[1] = _a[1];
   a[2] = _a[2];
   pB[0] = _pB[0];
   pB[1] = _pB[1]; 
}

// Helper method for absolute valuing double data types
double Kinematics3::myAbs(double x)
{
   if(x < 0)
      x = -1.0 * x;

   return x;
}


