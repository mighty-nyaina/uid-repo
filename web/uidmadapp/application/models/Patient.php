<?php
/**
 * Created by PhpStorm.
 * User: Ntsou
 * Date: 12/04/2016
 * Time: 19:20
 */
class Patient extends CI_Model{
    function __construct()
    {
        // Construct the parent class
        parent::__construct();
    }
    public function findNearestHospital($longitude, $latitude)
    {
        $sql = "SELECT nom,latitude, longitude, email,SQRT(
                    POW(69.1 * (latitude - ?), 2) +
                    POW(69.1 * (? - longitude) * COS(latitude / 57.3), 2)) AS distance
                FROM HOPITAL ORDER BY distance asc LIMIT 1";
        $query = $this->db->query($sql,array($latitude, $longitude));
        $result = $query->result();
        return $result;
    }
    public function getInfoClientByTag($id_tag)
    {        
        $retour = $this->db->select('*')
                        ->from("view_tag_patient")
                        ->where('ID_TAG', $id_tag)
                        ->get()->result();
        return $retour;
    }
    public function getMaladieClient($idpatient)
    {
         $retour = $this->db->select('*')
                        ->from("view_patient_maladie")
                        ->where('PATIENT_ID_PATIENT', $idpatient)
                        ->get()->result();
        return $retour;
    }
}