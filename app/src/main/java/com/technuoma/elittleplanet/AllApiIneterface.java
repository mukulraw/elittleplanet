package com.technuoma.elittleplanet;

import com.technuoma.elittleplanet.addressPOJO.addressBean;
import com.technuoma.elittleplanet.brandPOJO.brandBean;
import com.technuoma.elittleplanet.cartPOJO.cartBean;
import com.technuoma.elittleplanet.checkPromoPOJO.checkPromoBean;
import com.technuoma.elittleplanet.checkoutPOJO.checkoutBean;
import com.technuoma.elittleplanet.couponsPOJO.couponsBean;
import com.technuoma.elittleplanet.filtersPOJO.filtersBean;
import com.technuoma.elittleplanet.homePOJO.homeBean;
import com.technuoma.elittleplanet.notiPOJO.notiBeam;
import com.technuoma.elittleplanet.orderDetailsPOJO.orderDetailsBean;
import com.technuoma.elittleplanet.ordersPOJO.ordersBean;
import com.technuoma.elittleplanet.productsPOJO.productsBean;
import com.technuoma.elittleplanet.ratingsPOJO.ratingsBean;
import com.technuoma.elittleplanet.searchPOJO.searchBean;
import com.technuoma.elittleplanet.seingleProductPOJO.singleProductBean;
import com.technuoma.elittleplanet.subCat1POJO.subCat1Bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {

    @GET("elittleplanet/api/getBrands.php")
    Call<brandBean> getBrands();

    @GET("elittleplanet/api/getFilters.php")
    Call<filtersBean> getFilters();

    @Multipart
    @POST("elittleplanet/api/getHome2.php")
    Call<homeBean> getHome(
            @Part("lat") String lat,
            @Part("lng") String lng
    );

    @Multipart
    @POST("elittleplanet/api/getSubCat1.php")
    Call<subCat1Bean> getSubCat1(
            @Part("cat") String cat
    );

    @Multipart
    @POST("elittleplanet/api/getSubCat2.php")
    Call<subCat1Bean> getSubCat2(
            @Part("subcat1") String cat
    );

    @Multipart
    @POST("elittleplanet/api/getOrderId.php")
    Call<payBean> getOrderId(
            @Part("amount") String amount,
            @Part("receipt") String receipt
    );

    @Multipart
    @POST("elittleplanet/api/getProducts.php")
    Call<productsBean> getProducts(
            @Part("subcat2") String cat,
            @Part("location_id") String location_id
    );

    @Multipart
    @POST("elittleplanet/api/findPhone.php")
    Call<productsBean> findPhone(
            @Part("min") String min,
            @Part("max") String max,
            @Part("brand") String brand,
            @Part("ram") String ram,
            @Part("internal_storage") String internal_storage,
            @Part("network") String network,
            @Part("os") String os,
            @Part("camera") String camera,
            @Part("battery") String battery
    );

    @Multipart
    @POST("elittleplanet/api/getProductById.php")
    Call<singleProductBean> getProductById(
            @Part("id") String cat,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/addWishlist.php")
    Call<singleProductBean> addWishlist(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("elittleplanet/api/removeWishlist.php")
    Call<singleProductBean> removeWishlist(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("elittleplanet/api/search.php")
    Call<searchBean> search(
            @Part("query") String query,
            @Part("location_id") String location_id
    );


    @Multipart
    @POST("elittleplanet/api/login4.php")
    Call<loginBean> login(
            @Part("email") String email,
            @Part("password") String password,
            @Part("name") String name,
            @Part("token") String token
    );

    @Multipart
    @POST("elittleplanet/api/register.php")
    Call<loginBean> register(
            @Part("email") String email,
            @Part("password") String password,
            @Part("token") String token,
            @Part("phone") String phone,
            @Part("name") String name,
            @Part("address") String address,
            @Part("pin_code") String pin_code,
            @Part("referrer") String referrer
    );

    @Multipart
    @POST("elittleplanet/api/verify.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("elittleplanet/api/addCart.php")
    Call<singleProductBean> addCart(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price,
            @Part("version") String version,
            @Part("size") String size,
            @Part("color") String color
    );

    @Multipart
    @POST("elittleplanet/api/addAddress.php")
    Call<singleProductBean> addAddress(
            @Part("user_id") String user_id,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("name") String name
    );

    @Multipart
    @POST("elittleplanet/api/editAddress.php")
    Call<singleProductBean> editAddress(
            @Part("id") String id,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("name") String name
    );

    @Multipart
    @POST("elittleplanet/api/updateCart.php")
    Call<singleProductBean> updateCart(
            @Part("id") String id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price
    );

    @Multipart
    @POST("elittleplanet/api/deleteCart.php")
    Call<singleProductBean> deleteCart(
            @Part("id") String id
    );

    @Multipart
    @POST("elittleplanet/api/getRew.php")
    Call<String> getRew(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/getCoupons.php")
    Call<couponsBean> getCoupons(
            @Part("user_id") String user_id
    );


    @Multipart
    @POST("elittleplanet/api/clearCart.php")
    Call<singleProductBean> clearCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/getOrderDetails.php")
    Call<orderDetailsBean> getOrderDetails(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("elittleplanet/api/getCart.php")
    Call<cartBean> getCart(
            @Part("user_id") String user_id,
            @Part("location") String location,
            @Part("latitude") String latitude,
            @Part("longitude") String longitude
    );

    @Multipart
    @POST("elittleplanet/api/getNotification.php")
    Call<notiBeam> getNotification(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/getAddress.php")
    Call<addressBean> getAddress(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/deleteAddress.php")
    Call<addressBean> deleteAddress(
            @Part("id") String id
    );

    @Multipart
    @POST("elittleplanet/api/checkPromo.php")
    Call<checkPromoBean> checkPromo(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/buyVouchers.php")
    Call<checkoutBean> buyVouchers(
            @Part("user_id") String user_id,
            @Part("lat") String lat,
            @Part("lng") String lng,
            @Part("amount") String amount,
            @Part("txn") String txn,
            @Part("del_charges") String del_charges,
            @Part("wallet_amount") String wallet_amount,
            @Part("name") String name,
            @Part("phone") String phone,
            @Part("address") String address,
            @Part("pay_mode") String pay_mode,
            @Part("slot") String slot,
            @Part("date") String date,
            @Part("promo") String promo,
            @Part("promo_amount") String promo_amount,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("isnew") String isnew
    );

    @Multipart
    @POST("elittleplanet/api/getLogs.php")
    Call<trackBean> getLogs(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("elittleplanet/api/getWishlist.php")
    Call<orderDetailsBean> getWishlist(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/getRating.php")
    Call<ratingsBean> getRating(
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("elittleplanet/api/checkPurchase.php")
    Call<ratingsBean> checkPurchase(
            @Part("product_id") String product_id,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/addRating.php")
    Call<ratingsBean> addRating(
            @Part("product_id") String product_id,
            @Part("rating") String rating,
            @Part("review") String review,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/checkLoaderRating.php")
    Call<ratingsBean> checkLoaderRating(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("elittleplanet/api/submitLoaderRating.php")
    Call<ratingsBean> submitLoaderRating(
            @Part("id") String id,
            @Part("loader_rating") String loader_rating
    );

}
