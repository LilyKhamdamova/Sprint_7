package org.example.order;

public class CouriersForOrderRequest {
   public int getCourierId() {
      return courierId;
   }

   public void setCourierId(int courierId) {
      this.courierId = courierId;
   }

   public CouriersForOrderRequest(int courierId) {
      this.courierId = courierId;
   }

   private int courierId;

   public String getNearestStation() {
      return nearestStation;
   }

   public void setNearestStation(String nearestStation) {
      this.nearestStation = nearestStation;
   }

   public CouriersForOrderRequest(int courierId, String nearestStation) {
      this.courierId = courierId;
      this.nearestStation = nearestStation;
   }

   public CouriersForOrderRequest(String nearestStation) {
      this.nearestStation = nearestStation;
   }

   private String nearestStation;
}
