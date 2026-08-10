#include "../include/common/IO.hpp"

#include <iostream>

namespace common {

    namespace IO {

        // Prints a message to the console. This message
        // will not be formatted. 
        void print(const std::string &message) {
            std::cout << message;
        }

        // Prints a message as a line to the console. This will
        // produce the message and a new line. 
        void println(const std::string &message) {
            std::cout << message << "\n";
        }

        // Reads the next line from the user.
        std::string readString() {
            std::string r;
            std::cin >> r;

            return r;
        }
        
        // Reads the next integer from the user.
        int readInt() {
            int r;
            std::cin >> r;

            return r;
        }
        
        // Reads the next double from the user. 
        double readDouble() {
            double r;
            std::cin >> r;

            return r;
        }

        // Prompts the user with a message. We will then read
        // the user's response to the message as a string.
        std::string prompt(const std::string &message) {
            std::cout << message;
            
            std::string response;
            std::getline(std::cin, response);

            return response;
        }

        // Requests an int from the user. Prints a message and
        // then reads the next int. 
        int promptInt(const std::string &message) {
            print(message);
            return readInt();
        }

        // Requests an double from the user. Prints a message and
        // then reads the next double.
        double promptDouble(const std::string &message) {
            print(message);
            return readDouble();
        }

        // Trims the command of whitespace. 
        static std::string trimCommand(const std::string& command) {
            static const std::string whitespace = " \t\n\r\f\v";
            const auto strBegin = command.find_first_not_of(whitespace);

            if (strBegin == std::string::npos) {
                return "";
            }

            const auto strEnd = command.find_last_not_of(whitespace);
            const auto strRange = strEnd - strBegin + 1;

            return command.substr(strBegin, strRange);
        }

        // Makes all characters in a string capitalized.
        static std::string toUpper(const std::string& command) {
            std::string out = command;

            for (char& c : out) {
                c = static_cast<char>(std::toupper(static_cast<unsigned char>(c)));
            }

            return out;
        }

        // Splits a string based off a delimiter
        static std::vector<std::string> stringSplit(const std::string& input, const std::string& delimiter) {
            std::vector<std::string> tokens;
            size_t start = 0;
            size_t end = input.find(delimiter);

            while (end != std::string::npos) {
                tokens.push_back(input.substr(start, end - start));
                start = end + delimiter.length();
                end = input.find(delimiter, start);
            }

            tokens.push_back(input.substr(start));
            return tokens;
        }

        // Reads a command from the user. This method
        // verifies it is of the correct length, and 
        // is an actual valid command based off the
        // COMMANDS string.
        std::vector<std::string> readCommand() {
            std::string line = prompt("Enter your next command: ");
            line = trimCommand(line);

            if (line.empty()) {
                warning("No command entered");
                return {};
            }

            std::vector<std::string> parts = stringSplit(line, " ");
            parts[0] = toUpper(parts[0]);
            std::string command = parts[0];

            for (int i = 0; i < COMMANDS.size(); i += 3) {
                if (COMMANDS.substr(i, 3) == command)
                    return parts;
            }

            warning("Invalid command was entered: " + command);

            println("Valid commands: ");
            println(" - NAV (Sets a course)");
            println(" - SRS (Scans the current quadrant)");
            println(" - LRS (Scans all quadrants around you)");
            println(" - PHA (Fires your phasers)");
            println(" - TOR (Fires a torpedoe)");
            println(" - SHE (Raises/lowers the shields)");
            println(" - DAM (Gets the damage report)");
            println(" - COM (Access the library's computer)");
            println(" - XXX (Quits the game)");   

            return {};
        }

        std::vector<int> separateByCommas(const std::string& str) {
            std::string cleaned = "";
            
            for (const char& c : str) {
                if (std::isdigit(c) || c == ',')
                    cleaned += c;
            }

            std::stringstream ss(cleaned);
            std::string token;
            std::vector<int> numbers;

            while (std::getline(ss, token, ',')) {
                if (!token.empty())
                    numbers.push_back(std::stoi(token));
            }

            return numbers;
        }

        // Checks whether a number is invalid. Takes base-1 
        // coordinates. Will return true if the number is an invalid
        // coordinate. 
        bool invalidPosition(int num) { 
            return num > 8 || num < 1;
        }

        // Prompts the user to input a valid location within the
        // quadrant. Checks that they are valid, converts them to
        // (column, row) from (row, column) and validates their positions
        Location promptLocation() {
            print("Input a location (row, column), (row, column): ");

            std::string input;
            std::cin >> input;
            
            std::vector<int> numbers = separateByCommas(input);
            if (numbers.size() != 4) {
                warning("A location must have 4 coordinates");
                return {-1, -1, -1, -1};
            } 

            if (invalidPosition(numbers[0]) || invalidPosition(numbers[1]) ||
                invalidPosition(numbers[2]) || invalidPosition(numbers[3])) {
                warning("Number is invalid, try again");
                return {-1, -1, -1, -1};
            }

            // converts from (row, column) to (column, row) by switching the numbers
            return {toBase0(numbers[1]), toBase0(numbers[0]), toBase0(numbers[3]), toBase0(numbers[2])};
        }

        // Prompts the user to enter a valid sector using the (row, column) convention.
        // Will return a base-0 coordinate using the Location class. 
        Location promptSector() {
            print("Input a sector (row, column): ");

            std::string input;
            std::cin >> input;
            
            std::vector<int> numbers = separateByCommas(input);
            if (numbers.size() != 2) {
                warning("A location must have 2 coordinates");
                return {-1, -1, -1, -1};
            } 

            if (invalidPosition(numbers[0]) || invalidPosition(numbers[1])) {
                warning("Number is invalid, try again");
                return {-1, -1, -1, -1};
            }

            // converts from (row, column) to (column, row) by switching the numbers
            return {toBase0(numbers[1]), toBase0(numbers[0]), -1, -1};
        }

        // Prompts the user to enter a valid quadrant using the (row, column) convention.
        // Wil return a base-0 coordinate using the Location class. 
        Location promptQuadrant() {
            print("Input a quadrant (row, column): ");

            std::string input;
            std::cin >> input;
            
            std::vector<int> numbers = separateByCommas(input);
            if (numbers.size() != 2) {
                warning("A location must have 2 coordinates");
                return {-1, -1, -1, -1};
            } 

            if (invalidPosition(numbers[0]) || invalidPosition(numbers[1])) {
                warning("Number is invalid, try again");
                return {-1, -1, -1, -1};
            }

            // converts from (row, column) to (column, row) by switching the numbers
            return {-1, -1, toBase0(numbers[1]), toBase0(numbers[0])};
        }

        // Logs a trace message. 
        void trace(const std::string& message) {
            LOGGER.log(LogLevel::Trace, message);
        }

        // Logs a warning message. 
        void warning(const std::string& message) {
            LOGGER.log(LogLevel::Warning, message);
        }

        // Logs an error message. 
        void error(const std::string& message) {
            LOGGER.log(LogLevel::Error, message);
        }

    } // namespace IO

} // namepsace common