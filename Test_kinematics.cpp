/* PURPOSE: Finds the robotic configurations at points R and S with respect to the
 * given the parameters discussed in Lab1. Tests the j2c and c2j fuctions defined/described
 * in Kinematics3.h and Kinematics3.cpp.
 * AUTHOR: Melton, Connor
 * CLASS: Robotics, CS556
 * TEACHER: Vuskovic, Marko
 * ASSIGNMENT: LAB1
 * DUE: March 3rd, 2016
*/

#include "Kinematics3.h"

int main()
{

//Prompting user for starting configuration
   double qinit[N] = {0, 0, 0};
   printf("Please enter starting configuration for lab1:\n");
   scanf("%lf, %lf, %lf", &qinit[0], &qinit[1], &qinit[2]);
   
//Finding end-effector orientation
   double m = (Ry - Sy) / (Rx - Sx);
   double Q = 90 - atan(-1 * m) / d2r;

//Entering given lab values
   double a[N] = {500, 500, 100};
   double pB[2] = {0, 0};
   
   Kinematics3 lab1(a[0], a[1], a[2], pB[0], pB[1]);
 
//Finding configuration at point R
   double p3[N], qR[N];
   double *fi;
   fi = &p3[0];
   
   p3[0] = Rx - a[2] * cos(Q * d2r);
   p3[1] = Ry - a[2] * sin(Q * d2r);
   p3[2] = Q;
   lab1.c2j(qR, p3, fi, qinit);

//Finding link poses at configuration qR
   double J2Cresult[N^N];
   lab1.j2c(qR, J2Cresult, fi);
   
//Displaying c2j and j2c results for point R.
   printf("Configuration at point R: %lf, %lf, %lf\n", qR[0], qR[1], qR[2]);
   printf("Link poses at point R: \n");
   printf("\n%lf  %lf  %lf\n", J2Cresult[0], J2Cresult[1], J2Cresult[2]);
   printf("%lf  %lf  %lf\n", J2Cresult[3], J2Cresult[4], J2Cresult[5]);
   printf("%lf  %lf  %lf\n\n", J2Cresult[6], J2Cresult[7], J2Cresult[8]);
   
   Q = qR[0] + qR[1] + qR[2];

//Checking c2j results match expected values
if(J2Cresult[2] >= Rx + 0.01 || J2Cresult[2] <= Rx - 0.01){
    printf("Error: c2j did not produce expected x value.");
    return 1;
}
if(J2Cresult[5] >= Ry + 0.01 || J2Cresult[5] <= Ry - 0.01){
    printf("Error: c2j did not produce expected y value.");
    return 2;
}
if(J2Cresult[8] >= Q + 0.01 || J2Cresult[8] <= Q - 0.01){
    printf("Error: c2j did not produce expected wrist angle.\n");
    printf("%lf", Q);
    return 3;
}

//Finding configuration at point S
   double qS[N];
   
   p3[0] = Sx - a[2] * cos(Q * d2r);
   p3[1] = Sy - a[2] * sin(Q * d2r);
   p3[2] = Q;

   lab1.setbase(pB);
   lab1.setkin(a);
   lab1.c2j(qS, p3, fi, qR);
   
//Finding link poses at qS configuration
   lab1.j2c(qS, J2Cresult, fi);
   Q = qS[0] + qS[1] + qS[2];
   
//Displaying c2j and j2c results for point S.
   printf("Configuration at point S: %lf, %lf, %lf\n", qS[0], qS[1], qS[2]);
   printf("Link poses at point S: \n");
   printf("\n%lf  %lf  %lf\n", J2Cresult[0], J2Cresult[1], J2Cresult[2]);
   printf("%lf  %lf  %lf\n", J2Cresult[3], J2Cresult[4], J2Cresult[5]);
   printf("%lf  %lf  %lf\n", qS[0], qS[0] + qS[1], qS[0] + qS[1] + qS[2]);

//Checking c2j results match expected values
if(J2Cresult[2] >= Sx + 0.01 || J2Cresult[2] <= Sx - 0.01){
    printf("Error: c2j did not produce expected x value.");
    return 1;
}
if(J2Cresult[5] >= Sy + 0.01 || J2Cresult[5] <= Sy - 0.01){
    printf("Error: c2j did not produce expected y value.");
    return 2;
}
if(J2Cresult[8] >= Q + 0.01 || J2Cresult[8] <= Q - 0.01){
    printf("Error: c2j did not produce expected wrist angle.\n");
    printf("%lf", Q);
    return 3;
}
   return 0;
}
