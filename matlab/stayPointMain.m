% gps=load('D:\Research\data\MSR GPS Privacy Dataset 2009\userDataCSV\gps_volunteer_04.csv',1);
gps=csvread('D:\Research\data\MSR GPS Privacy Dataset 2009\userDataCSV\gps_volunteer_04.csv');
stayPoint = stay_point_GPS(gps(:,3), gps(:,1), gps(:,4), gps(:,1));
stayPoint(1, :)