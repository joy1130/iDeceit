function [stayPoint]=stay_point_GPS(latitude_sample,latitude_timestamp,longitude_sample,longitude_timestamp,Tmax,Tmin,Dmax)
stayPoint=[];
i=1;
if nargin < 7
    Dmax =100;     % meter
end
if nargin < 6
    Tmin =2*60;    %second
end
if nargin < 5
    Tmax =30*60; %second
end
while i<length(latitude_sample)
%     i
    j=i+1;
    if j==length(longitude_sample)
        return;
    end
    while j<length(latitude_sample)
%         j
        t=(latitude_timestamp(j)-latitude_timestamp(j-1))/1000;
        if t>Tmax
%             if t>Tmin
%                 sp=getStayPoint( latitude_sample, longitude_sample, latitude_timestamp,i,j );
%                 if (sp(1,4)-sp(1,3))/1000/60<10
%                         i
%                         j
%                         disp('1st if');
%                 end
%                 stayPoint =[stayPoint; sp];
%             end
            i=j;
            break;
        end
        d=calculateHaversineDistance( latitude_sample(i), longitude_sample(i), latitude_sample(j), longitude_sample(j) );
        if d>Dmax
            t=(latitude_timestamp(j-1)-latitude_timestamp(i))/1000;
            if t>Tmin
                sp=getStayPoint( latitude_sample, longitude_sample, latitude_timestamp,i,j );
                if (sp(1,4)-sp(1,3))/1000/60<10
                        i
                        j
                        disp('2nd if');
                 end
                stayPoint =[stayPoint; sp];
%                 i=j;
%                 break;
            end
            i=j;
            break;
        else
            if j==length(latitude_sample)-1
                t=(latitude_timestamp(j-1)-latitude_timestamp(i))/1000;
                if t>Tmin
                    sp=getStayPoint( latitude_sample, longitude_sample, latitude_timestamp,i,j );
                    if (sp(1,4)-sp(1,3))/1000/60<10
                        i
                        j
                        disp('3rd if');
                    end
                    stayPoint =[stayPoint; sp];
                end
                i=j;
                break;
            end
        end
        j=j+1;
    end
%     if i~=j
%         i=i+1;
%     end
end

end

function distance = calculateHaversineDistance( lat1, long1, lat2, long2 )
radian = 57.2958;
distance = 6371*acos(cos(long1/radian-long2/radian)*cos(lat1/radian)*cos(lat2/radian)+sin(lat1/radian)*sin(lat2/radian));
distance = distance * 1000; % meter
end

function sp = getStayPoint( latitude_sample, longitude_sample, latitude_timestamp,i,j )
    sp=[];
    lat=mean(latitude_sample(i:j));
    lon=mean(longitude_sample(i:j));
    t_start=latitude_timestamp(i);
    t_end=latitude_timestamp(j-1);
    sp=[lat lon t_start t_end];  
end