package com.krypt.Hubble.utils;

public class Urls {


  public static String ipAddress = "http://hubble.blupayinc.com/";
//  public static String ipAddress = "http://jantech.blupayinc.com/";
    public static final String ids ="";
    private static final String ROOT_URL = ipAddress + "api_files/";
    //project manager
    public static final String CREATE_EVENTS = ROOT_URL + "project_manager/createevents.php";
    public static final String GET_EVENTS = ROOT_URL + "project_manager/getevents.php";
    //Trainees
    public static final String URL_TRAINEE= ROOT_URL+"trainee/get_trainee.php";
    //tech
     public static final String URL_ASSIGNMENT_TECH= ROOT_URL + "driver/assignmentstech.php";

    //  products
    public static final String URL_GET_SERVICES= ROOT_URL + "customers/services.php";
    public static final String URL_GET_PRODUCTS = ROOT_URL + "customers/products.php";
    public static final String URL_ADD_CART = ROOT_URL + "customers/add_to_cart.php";
    public static final String URL_ADD_CARTSCERV= ROOT_URL + "customers/addtocartserv.php";
    public static final String URL_GET_CART = ROOT_URL + "customers/cart.php";
    public static final String URL_GET_SERVICECART = ROOT_URL + "customers/servicescart.php";
    public static final String URL_UPDATE_CART = ROOT_URL + "customers/car_update.php";
    public static final String URL_REMOVE_ITEM = ROOT_URL + "customers/cart_remove.php";
    // shipping
    public static final String URL_SUBMIT_SHIPPING = ROOT_URL + "customers/submit_shipping.php";
    public static final String URL_GET_COUNTIES = ROOT_URL + "customers/counties.php";
    public static final String URL_GET_TOWNS = ROOT_URL + "customers/towns.php";
    public static final String URL_DELIVERY_DETAILS = ROOT_URL + "customers/delivery_details.php";
    // checkout
    public static final String URL_GET_CHECKOUT_TOTAL = ROOT_URL + "customers/checkout_cost.php";
    // user
    public static final String URL_REGISTER = ROOT_URL + "customers/register.php";
    public static final String URL_LOGIN = ROOT_URL + "select.php";
    public static final String URL_TECHNICIAN_ORDER=ROOT_URL + "customers/placerepair.php";
    // orders
    public static final String URL_MAKE_PAYMENT = ROOT_URL + "customers/submit_booking.php";
    public static final String URL_BOOKINGS = ROOT_URL + "customers/bookings.php";
    public static final String URL_BOOKING_ITEMS = ROOT_URL + "customers/items.php";
    public static final String URL_RECEIVED = ROOT_URL + "customers/mark_delivered.php";
    public static final String URL_COLLECT = ROOT_URL + "customers/mark_return.php";
    public static final String URL_GET_MY_FINES = ROOT_URL + "customers/my_fines.php";
    public static final String URL_PAY_FINE = ROOT_URL + "customers/pay_fines.php";
    //    FEEDBACK
    public static final String URL_SEND_FEEDBACK = ROOT_URL + "customers/send_feedback.php";
    public static final String URL_FEEDBACK = ROOT_URL + "customers/get_feedback.php";
    public static final String URL_SEND_FEEDBACKF = ROOT_URL + "customers/send_feedbackf.php";
    public static final String URL_FEEDBACKF = ROOT_URL + "customers/get_feedbackf.php";
    public static final String URL_SEN_FEEDBACK = ROOT_URL + "supervisor/sen_fed.php";
    public static final String URL_BACK = ROOT_URL + "supervisor/get_fed.php";
    public static final String URL_SENF_FEEDBACK = ROOT_URL + "finance/sen_fed.php";
    public static final String URL_FBACK = ROOT_URL + "finance/get_fed.php";
    //Staff
    //Staff
    public static final String URL_STAFF_LOGIN = ROOT_URL + "staff_login.php";
    //Finance
    public static final String URL_PENDING_BOOKINGS = ROOT_URL + "finance/pending_bookings.php";
    public static final String URL_APPROVED_BOOKINGS = ROOT_URL + "finance/approved_bookings.php";
    public static final String URL_ACCEPT_BOOKING = ROOT_URL + "finance/accept_booking.php";
    public static final String URL_REJECTED_BOOKINGS = ROOT_URL + "finance/rejected_bookings.php";
    public static final String URL_REJECT = ROOT_URL + "finance/reject_booking.php";
    public static final String URL_NEW_FINES = ROOT_URL + "finance/new_fines.php";
    public static final String URL_APPROVE_FINE = ROOT_URL + "finance/approve_fine.php";
    public static final String URL_APPROVED_FINES_PAY = ROOT_URL + "finance/approved_fine_pay.php";
    //shipping mrg
    public static final String URL_PENDING_DELIVERY = ROOT_URL + "ship/pending_delivery.php";
    public static final String URL_PENDING_DELI = ROOT_URL + "store_manager/hh.php";
    public static final String URL_PENDING_SERV = ROOT_URL + "ship/pending_serv.php";
    public static final String URL_PENDING_TECH = ROOT_URL + "ship/pending_deliverytech.php";
    public static final String URL_PENDING_SUP = ROOT_URL + "ship/pending_sup.php";

    public static final String URL_DRIVERS = ROOT_URL + "ship/get_drivers.php";
    public static final String URL_TECHS = ROOT_URL + "ship/get_techs.php";
    public static final String URL_SUPERVISOR = ROOT_URL + "ship/get_supers.php";
    public static final String URL_ASSIGN_DRIVERS = ROOT_URL + "ship/assign_driver.php";
    public static final String URL_ASSIGN_SUPER= ROOT_URL + "ship/assign_super.php";
    public static final String URL_ASSIGN_TECH= ROOT_URL + "ship/assign_tech.php";

    public static final String URL_SHIPPING = ROOT_URL + "ship/shipping.php";
    public static final String URL_DELIVERED = ROOT_URL + "ship/delivered.php";
    // Driver
    public static final String URL_ASSIGNMENT = ROOT_URL + "driver/assignments.php";
    public static final String URL_ISSUED = ROOT_URL + "driver/issued.php";
    public static final String URL_DELIVERY_HISTORY = ROOT_URL + "driver/delivery_history.php";
    public static final String URL_ASSIGNED_ITEMS = ROOT_URL + "driver/items_to_deliver.php";
    public static final String URL_ISSUE = ROOT_URL + "driver/mark_issue.php";
    public static final String URL_ITEMS_TO_RETURN = ROOT_URL + "driver/itemsToReturn.php";
    public static final String URL_MY_RETURN = ROOT_URL + "driver/my_returns.php";
    public static final String MARK_RETURN_BACK = ROOT_URL + "driver/mark_return_back.php";
    //Inventory mrg
    public static final String GET_STOCK = ROOT_URL + "store_manager/store_items.php";

    public static final String GET_STOCK_SUPPLIER = ROOT_URL + "store_manager/store_itemssup.php";

    public static final String URL_UPLOAD_STOCK = ROOT_URL + "store_manager/tostock.php";
    public static final String ADD_STOCK = ROOT_URL + "store_manager/add_new_stock.php";
    public static final String ADD_STOCK_SUP= ROOT_URL + "store_manager/add_new_stocksup.php";
    public static final String RETURNS_PENDING_CONFIRMATION = ROOT_URL + "store_manager/returns_pending_confirm.php";
    public static final String RETURNS_HISTORY = ROOT_URL + "store_manager/return_history.php";
    public static final String CONFIRM_RETURN = ROOT_URL + "store_manager/confirm_return.php";
    //    SUPERVISOR
    public static final String URL_PENDING_RETURN = ROOT_URL + "supervisor/pending_return.php";
    public static final String URL_RETURN_DRIVERS = ROOT_URL + "supervisor/get_drivers.php";
    public static final String ASSIGN_RETURN_DRIVERS = ROOT_URL + "supervisor/assign_return_driver.php";
    public static final String URL_SHIPPING_BACK = ROOT_URL + "supervisor/shipping_back.php";
    public static final String URL_RETURNED = ROOT_URL + "supervisor/returned.php";
    public static final String RETURN_FINE = ROOT_URL + "supervisor/submit_fine.php";
    public static final String URL_GET_FINES = ROOT_URL + "supervisor/fines.php";
    public static final String ROOT_URL_IMAGES = ipAddress + "upload_products/";


}
