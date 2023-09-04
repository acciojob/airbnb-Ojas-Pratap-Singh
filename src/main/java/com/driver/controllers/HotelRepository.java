package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelRepository {

    HashMap<String, Hotel> hotelDb = new HashMap<>();
    HashMap<Integer,User> userDb = new HashMap<>();
    HashMap<String,Booking> bookingDb = new HashMap<>();


    public String addHotel(Hotel hotel) {
        if(hotel==null || hotel.getHotelName()=="" || hotel.getHotelName()==null){
            return "FAILURE";
        }
        if(hotelDb.containsKey(hotel.getHotelName())){
            return "FAILURE";
        }
        hotelDb.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userDb.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        String ans = "";
        int max = 0;
        for(String name : hotelDb.keySet()){
            int currmax = hotelDb.get(name).getFacilities().size();
            if(currmax>max){
                max = currmax;
                ans = name;
            }else if(currmax == max && name.compareTo(ans)<0){
                    ans = name;
            }
        }
        if(max==0) return "";
        return ans;
    }

    public int bookARoom(Booking booking) {
        String bookid = String.valueOf(UUID.randomUUID());

        booking.setBookingId(bookid);
        String hname = booking.getHotelName();
        int roomavaiable = hotelDb.get(hname).getAvailableRooms();
        int priceofroom = hotelDb.get(hname).getPricePerNight();
        int noofroomtobeboked = booking.getNoOfRooms();
        if(roomavaiable<noofroomtobeboked) return -1;
        int price = noofroomtobeboked*priceofroom;
        hotelDb.get(hname).setAvailableRooms(roomavaiable - noofroomtobeboked);
        bookingDb.put(bookid,booking);
        hotelDb.put(hname,hotelDb.get(hname));

        return price;
    }


    public int getBookings(Integer aadharCard) {
        int ans =0;
        for(String bookid : bookingDb.keySet()){
            int curraadharCard = bookingDb.get(bookid).getBookingAadharCard();
            if(curraadharCard == aadharCard){
                ans++;
            }
        }
        return ans;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel = hotelDb.get(hotelName);
        List<Facility> currFacility = hotel.getFacilities();
        for(Facility facility : newFacilities){
            if(currFacility.contains(facility)){
                continue;
            }else{
                currFacility.add(facility);
            }
        }
        hotel.setFacilities(currFacility);
        hotelDb.put(hotelName,hotel);
        return hotel;
    }
}
